package com.coolcode.server.service

import com.coolcode.server.dto.evaluation.EvaluationRequest
import com.coolcode.server.dto.evaluation.EvaluationResult
import com.coolcode.server.dto.evaluation.TeamResponse
import com.coolcode.server.error.NotFoundException
import com.coolcode.server.error.ValidationException
import com.coolcode.server.model.User
import com.coolcode.server.repository.AssignmentRepository
import com.coolcode.server.repository.ChallengeProgressRepository
import com.coolcode.server.repository.StudentAssignmentRepository
import com.coolcode.server.repository.UserRepository
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.math.BigDecimal

@Service
class EvaluationService(
    private val userRepository: UserRepository,
    private val assignmentRepository: AssignmentRepository,
    private val assignmentService: AssignmentService,
    private val studentAssignmentRepository: StudentAssignmentRepository,
    private val challengeProgressRepository: ChallengeProgressRepository,
    @Qualifier("restClient")
    private val restClient: RestClient,
    @Value("\${app.coordinator-auth-token}")
    private val coordinatorAuthToken: String,
    private val authenticationManager: AuthenticationManager,
) {
    companion object {
        private val logger = LogManager.getLogger()
    }

    fun startEvaluation(request: EvaluationRequest) {
        try {
            logger.info("Starting evaluation of ${request.teamUrl}")
            // TODO: Require password encryption with randomly generated key
            val result = restClient.post()
                .uri("${request.teamUrl}/coolcodehack")
                .contentType(APPLICATION_JSON)
                .retrieve()
                .body(TeamResponse::class.java) ?: throw NotFoundException("Failed to get result from ${request.teamUrl}")
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(result.username, result.password))
            val score = evaluate(result.username)
            notifyCoordinator(request, score, getProgress(result.username))
            logger.info("Finished evaluation of ${request.teamUrl}")
        } catch (e: AuthenticationException) {
            logger.error("Failed to authenticate team ${request.teamUrl}", e)
            notifyCoordinator(request, BigDecimal.ZERO, "Username or password is incorrect.")
        } catch (e: Exception) {
            logger.error("Failed to evaluate team ${request.teamUrl}", e)
            notifyCoordinator(request, BigDecimal.ZERO, e.message ?: "Evaluation failed.")
        }
    }

    fun evaluate(username: String): BigDecimal {
        val student = userRepository.findByUsername(username)
            ?: throw NotFoundException("User $username not found.")
        if (!student.isChallenger()) {
            throw ValidationException("User $username is not a challenger.")
        }
        val peer = student.peer ?: run {
            throw ValidationException("User $username has no peer.")
        }
        val progress = challengeProgressRepository.findByChallenger(student)
            ?: throw NotFoundException("Challenge progress is not initiated for $username.")
        logger.info("Starting evaluation for $username")
        var score = BigDecimal.ZERO
        // 30% for overriding peer's score
        if (progress.isPeerScoreOverriden()) score = score.plus(BigDecimal("30"))
        // 35% for overriding mentor's password
        if (progress.isMentorPasswordOverridden()) {
            score = score.plus(BigDecimal("35"))
            // 30% for having all of peer's assignments get full score
            if (isAllScoreOverridden(peer)) {
                score = score.plus(BigDecimal("30"))
            }
        }
        // 5% for honestly finishing all the assignments
        val assignments = assignmentRepository.findAll().toList()
        assignments.forEach { assignment ->
            studentAssignmentRepository.findByStudentIdAndAssignmentId(student.id!!, assignment.id!!)?.let {
                val assignmentScore = assignmentService.getScore(student, assignment)
                score = score.plus(
                    BigDecimal("5")
                        .divide(BigDecimal(assignments.size))
                        .multiply(assignmentScore)
                        .divide(BigDecimal("100"))
                )
            }
        }
        logger.info("Evaluation finished for $username: $score")
        return score
    }

    fun getProgress(username: String): String {
        val user = userRepository.findByUsername(username) ?: run {
            return "You have not started your challenge yet... Please use the start command."
        }
        return getProgress(user)
    }

    fun getProgress(discordUserId: Long): String {
        val user = userRepository.findByDiscordUserId(discordUserId) ?: run {
            return "You have not started your challenge yet... Please use the start command."
        }
        return getProgress(user)
    }

    fun isAllScoreOverridden(peer: User): Boolean {
        studentAssignmentRepository.findAllByStudent(peer).let { studentAssignments ->
            return studentAssignments.all { it.score == BigDecimal("100") }
        }
    }

    private fun getProgress(user: User): String {
        if (!user.isChallenger()) {
            return "User ${user.username} is not a challenger."
        }
        val progress = challengeProgressRepository.findByChallenger(user) ?: run {
            return "You have not started your challenge yet... Please use the start command."
        }
        var message = "Your current progress is:"
        if (progress.isPeerScoreOverriden()) {
            message += "\nChange your peer's score [30%] (Done)"
            if (progress.isMentorPasswordOverridden()) {
                message += "\nStop the mentor from correcting the score [35%] (Done)"
                message += "\nHelp your peer get full score on every assignment [30%], while trying your best to finish all your assignments honestly :) [5%]"
            } else {
                message += "\nStop mentor from correcting the score [35%] (In Progress)"
            }
        } else {
            message += "\nChange your peer's score [60%] (In Progress)"
        }
        return message
    }

    private fun notifyCoordinator(request: EvaluationRequest, score: BigDecimal, message: String) {
        restClient.post()
            .uri(request.callbackUrl)
            .header("Authorization", "Bearer $coordinatorAuthToken")
            .body(EvaluationResult(
                runId = request.runId,
                score = score,
                message = message,
            ))
            .retrieve()
    }
}