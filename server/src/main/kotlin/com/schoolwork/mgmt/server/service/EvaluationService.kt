package com.schoolwork.mgmt.server.service

import com.schoolwork.mgmt.server.error.NotFoundException
import com.schoolwork.mgmt.server.error.ValidationException
import com.schoolwork.mgmt.server.repository.AssignmentRepository
import com.schoolwork.mgmt.server.repository.ChallengeProgressRepository
import com.schoolwork.mgmt.server.repository.StudentAssignmentRepository
import com.schoolwork.mgmt.server.repository.UserRepository
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
        val progress = challengeProgressRepository.findByChallenger(student)
            ?: throw NotFoundException("Challenge progress is not initiated for $username.")
        logger.info("Starting evaluation for $username")
        var score = BigDecimal.ZERO
        // 60% for overriding peer's score
        if (progress.isPeerScoreOverriden()) score = score.plus(BigDecimal("60"))
        // 35% for overriding mentor's password
        if (progress.isMentorPasswordOverridden()) score = score.plus(BigDecimal("35"))
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
}