package com.coolcode.server.service

import com.coolcode.server.error.NotFoundException
import com.coolcode.server.error.ValidationException
import com.coolcode.server.repository.AssignmentRepository
import com.coolcode.server.repository.ChallengeProgressRepository
import com.coolcode.server.repository.StudentAssignmentRepository
import com.coolcode.server.repository.UserRepository
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class EvaluationService(
    private val userRepository: UserRepository,
    private val assignmentRepository: AssignmentRepository,
    private val assignmentService: AssignmentService,
    private val studentAssignmentRepository: StudentAssignmentRepository,
    private val challengeProgressRepository: ChallengeProgressRepository,
) {
    companion object {
        private val logger = LogManager.getLogger()
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
        if (progress.isMentorPasswordOverridden()) score = score.plus(BigDecimal("35"))
        // 30% for having all of peer's assignments get full score
        studentAssignmentRepository.findAllByStudent(peer).let { studentAssignments ->
            val isAllScoreFull = studentAssignments.all { it.score == BigDecimal("100") }
            if (isAllScoreFull) {
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

    fun getProgress(discordUserId: Long): String {
        val user = userRepository.findByDiscordUserId(discordUserId) ?: run {
            return "You have not started your challenge yet... Please use the start command."
        }
        if (!user.isChallenger()) {
            return "User with Discord ID $discordUserId is not a challenger."
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
}