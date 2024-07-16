package com.schoolwork.mgmt.server.service

import com.schoolwork.mgmt.server.dto.assignment.*
import com.schoolwork.mgmt.server.enum.AssignmentGrade
import com.schoolwork.mgmt.server.enum.DbBoolean
import com.schoolwork.mgmt.server.error.NotFoundException
import com.schoolwork.mgmt.server.error.UnauthorizedException
import com.schoolwork.mgmt.server.error.ValidationException
import com.schoolwork.mgmt.server.model.*
import com.schoolwork.mgmt.server.repository.*
import com.schoolwork.mgmt.server.security.UserRole
import org.apache.logging.log4j.LogManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class AssignmentService(
    private val assignmentRepository: AssignmentRepository,
    private val questionRepository: QuestionRepository,
    private val studentQuestionRepository: StudentQuestionRepository,
    private val studentAssignmentRepository: StudentAssignmentRepository,
    private val userRepository: UserRepository,
    private val challengeProgressRepository: ChallengeProgressRepository,
) {
    companion object {
        private val logger = LogManager.getLogger()
    }

    fun createAssignment(request: CreateAssignmentRequest) {
        logger.info("Creating the assignment")
        val now = LocalDateTime.now()
        val assignment = assignmentRepository.save(Assignment(
            title = request.title,
            level = request.level,
            deadline = request.deadline,
            createdAt = now,
        ))
        logger.info("Successfully created the assignment")
        request.questions.forEach { question ->
            questionRepository.save(Question(
                description = question.description,
                assignment = assignment,
                optionA = question.optionA,
                optionB = question.optionB,
                optionC = question.optionC,
                optionD = question.optionD,
                answer = question.answer,
                createdAt = now,
            ))
        }
        logger.info("Successfully created the questions")
    }

    fun deleteAssignment(assignmentId: Long) {
        val assignment = assignmentRepository.findByIdOrNull(assignmentId)
            ?: throw NotFoundException("Assignment with id $assignmentId not found")
        questionRepository.deleteAllByAssignment(assignment)
        assignmentRepository.delete(assignment)
    }

    fun getAllAssignments(self: User, username: String? = null): List<AssignmentDto> = assignmentRepository.findAll().toList().map { assignment ->
        val user = username?.let { userRepository.findByUsername(username) ?: throw NotFoundException("User $username not found.") } ?: self
        username?.let {
            if (user.id != self.id && user.peer?.id != self.id) throw ValidationException("User ${self.username} is not allowed to view ${user.username}'s assignments.")
        }
        val questions = questionRepository.findAllByAssignment(assignment)
        val finishCount = studentQuestionRepository.countByStudentIdAndQuestionIdIn(user.id!!, questions.map { it.id!! })
        val studentAssignment = studentAssignmentRepository.findByStudentIdAndAssignmentId(user.id, assignment.id!!)

        AssignmentDto(
            id = assignment.id,
            title = assignment.title,
            level = assignment.level.displayName,
            deadline = assignment.deadline.atZone(ZoneOffset.UTC),
            questionCount = questions.size,
            finishCount = finishCount,
            submitted = studentAssignment != null,
            grade = studentAssignment?.score?.let { getGrade(it) },
            score = studentAssignment?.score,
        )
    }

    fun getAssignment(self: User, assignmentId: Long): AssignmentWithQuestionsDto = assignmentRepository
        .findByIdOrNull(assignmentId)?.let { assignment ->
            val studentAssignment = studentAssignmentRepository.findByStudentIdAndAssignmentId(self.id!!, assignment.id!!)
            AssignmentWithQuestionsDto(
                id = assignment.id,
                title = assignment.title,
                level = assignment.level.displayName,
                deadline = assignment.deadline.atZone(ZoneOffset.UTC),
                submitted = studentAssignment != null,
                score = studentAssignment?.score,
                questions = questionRepository.findAllByAssignment(assignment).map { question ->
                    QuestionDto(
                        id = question.id!!,
                        description = question.description,
                        optionA = question.optionA,
                        optionB = question.optionB,
                        optionC = question.optionC,
                        optionD = question.optionD,
                        answer = studentQuestionRepository.findByStudentIdAndQuestionId(self.id, question.id)?.answer
                    )
                }
            )
        } ?: throw NotFoundException("Assignment $assignmentId not found.")

    fun saveAssignmentProgress(self: User, request: SubmitAssignmentRequest) {
        logger.info("Saving the progress for ${self.username} of assignment ${request.id}")
        submitAssignment(self, request, false)
        logger.info("Successfully saved the progress for ${self.username} of assignment ${request.id}")
    }

    fun submitAssignment(self: User, request: SubmitAssignmentRequest) {
        logger.info("Submitting the assignment ${request.id} for ${self.username}")
        submitAssignment(self, request, true)
        logger.info("Successfully submitted the assignment for ${self.username} and is ready for scoring")
    }

    fun withdrawSubmission(self: User, assignmentId: Long) {
        logger.info("Withdrawing the submission for ${self.username} of assignment $assignmentId")
        assignmentRepository.findByIdOrNull(assignmentId)?.let {
            validateDeadline(it)
        } ?: run {
            throw NotFoundException("Assignment with id $assignmentId not found.")
        }
        val existingStudentAssignment = studentAssignmentRepository
            .findByStudentIdAndAssignmentId(self.id!!, assignmentId)
            ?: throw NotFoundException("Assignment submission record is not found for ${self.username} of assignment $assignmentId.")
        studentAssignmentRepository.delete(existingStudentAssignment)
        logger.info("Successfully withdrew the submission for ${self.username} of assignment $assignmentId")
    }

    // The function for challengers to call to override score
    fun uploadScore(self: User, request: UploadScoreRequest) {
        if (self.role !== UserRole.MENTOR) throw UnauthorizedException("User ${self.username} is not a mentor.")
        if (request.score > BigDecimal(100) || request.score < BigDecimal(0)) {
            throw ValidationException("Score should be between 0 and 100.")
        }

        // Validate mentor-student
        val student = userRepository.findByUsernameAndRole(request.username, UserRole.STUDENT)
            ?: throw NotFoundException("Student with username ${request.username} not found.")
        logger.info("Uploading score for ${student.username} of assignment ${request.assignmentId}")
        if (student.mentor?.id != self.id) {
            throw UnauthorizedException("User ${self.username} is not student ${student.username}'s mentor.")
        }

        // Override score
        val assignment = assignmentRepository.findByIdOrNull(request.assignmentId)
            ?: throw NotFoundException("Assignment ${request.assignmentId} not found.")
        val now = LocalDateTime.now()
        studentAssignmentRepository.findByStudentIdAndAssignmentId(student.id!!, assignment.id!!)?.also {
            it.score = request.score
            it.updatedAt = now
            studentAssignmentRepository.save(it)
        } ?: run { throw NotFoundException("Student ${student.username} hasn't submitted the assignment ${assignment.id}") }

        // Update progress only if overriding peer's score
        if (!student.isChallenger()) {
            val challenger = userRepository.findByMentorAndIsChallenger(self, DbBoolean.Y)
                ?: throw NotFoundException("There is no challenger under mentor ${self.username}.")
            challengeProgressRepository.findByChallenger(challenger)?.also {
                it.peerScoreOverridden = DbBoolean.Y
                it.updatedAt = now
                challengeProgressRepository.save(it)
            } ?: run { throw NotFoundException("Challenge progress is not initiated for ${challenger.username}.") }
        }

        logger.info("Successfully uploaded score for ${student.username} of assignment ${request.assignmentId}")
    }

    // Better not to throw error during a scheduled job.
    fun mentorTryingToCorrectScore() {
        userRepository.findAllByRole(UserRole.MENTOR).forEach { mentor ->
            val challenger = userRepository.findByMentorAndIsChallenger(mentor, DbBoolean.Y)
                ?: run {
                    logger.error("There is no challenger under mentor ${mentor.username}.")
                    return@forEach
                }
            val progress = challengeProgressRepository.findByChallenger(challenger)
                ?: run {
                    logger.error("Challenge progress is not initiated for ${challenger.username}.")
                    return@forEach
                }
            if (progress.mentorPasswordOverridden == DbBoolean.Y) {
                logger.info("Mentor ${mentor.username}'s password has been overridden and is unable to correct the score.")
                return@forEach
            }

            userRepository.findAllByMentor(mentor).forEach { student ->
                studentAssignmentRepository.findAllByStudent(student).forEach { studentAssignment ->
                    logger.info("Mentor ${mentor.username} is trying to correct ${studentAssignment.student.username}'s score ${studentAssignment.score} of assignment ${studentAssignment.assignment.id}.")
                    val score = getScore(studentAssignment.student, studentAssignment.assignment)
                    studentAssignment.score = score
                    studentAssignmentRepository.save(studentAssignment)
                    logger.info("Mentor ${mentor.username} successfully corrected ${studentAssignment.student.username}'s score ${studentAssignment.score} of assignment ${studentAssignment.assignment.id}.")
                }
            }
        }
    }

    private fun validateDeadline(assignment: Assignment) {
        if (!assignment.deadline.isAfter(LocalDateTime.now())) {
            throw ValidationException("Deadline has passed for submission of assignment ${assignment.id}.")
        }
    }

    private fun submitAssignment(self: User, request: SubmitAssignmentRequest, readyForScore: Boolean) {
        val assignment = assignmentRepository.findByIdOrNull(request.id)?.let {
            validateDeadline(it)
            it
        } ?: run {
            throw NotFoundException("Assignment with id ${request.id} not found")
        }
        val now = LocalDateTime.now()
        // Submit question answers
        request.questions.forEach { question ->
            val existingStudentQuestion = studentQuestionRepository.findByStudentIdAndQuestionId(self.id!!, question.id)
            if (existingStudentQuestion != null) {
                question.answer?.let {
                    existingStudentQuestion.answer = question.answer
                    existingStudentQuestion.updatedAt = now
                    studentQuestionRepository.save(existingStudentQuestion)
                } ?: run {
                    studentQuestionRepository.delete(existingStudentQuestion)
                }
            } else {
                question.answer?.let {
                    studentQuestionRepository.save(StudentQuestion(
                        studentId = self.id,
                        questionId = question.id,
                        answer = question.answer,
                        createdAt = now,
                        updatedAt = now,
                    ))
                }
            }
        }
        if (readyForScore) {
            // Keep assignment submission record
            val existingStudentAssignment = studentAssignmentRepository.findByStudentIdAndAssignmentId(self.id!!, request.id)
            if (existingStudentAssignment != null) {
                existingStudentAssignment.score = null
                existingStudentAssignment.updatedAt = now
                studentAssignmentRepository.save(existingStudentAssignment)
            } else {
                studentAssignmentRepository.save(StudentAssignment(
                    student = self,
                    assignment = assignment,
                    createdAt = now,
                    updatedAt = now,
                ))
            }
        }
    }

    fun getScore(student: User, assignment: Assignment): BigDecimal {
        val questions = questionRepository.findAllByAssignment(assignment)
        val studentQuestionsByQuestionId = studentQuestionRepository
            .findAllByStudentIdAndQuestionIdIn(student.id!!, questions.map { it.id!! })
            .associateBy { it.questionId }
        var correctCount = 0
        questions.forEach { question ->
            if (studentQuestionsByQuestionId.containsKey(question.id!!)) {
                val studentQuestion = studentQuestionsByQuestionId[question.id]!!
                if (studentQuestion.answer == question.answer) {
                    correctCount++
                }
            }
        }
        return BigDecimal(correctCount).divide(BigDecimal(questions.size), 2, RoundingMode.HALF_UP).multiply(BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)
    }

    private fun getGrade(score: BigDecimal): AssignmentGrade {
        if (score > BigDecimal(90)) {
            return AssignmentGrade.A
        }
        if (score > BigDecimal(80)) {
            return AssignmentGrade.B
        }
        if (score > BigDecimal(70)) {
            return AssignmentGrade.C
        }
        if (score > BigDecimal(60)) {
            return AssignmentGrade.D
        }
        if (score > BigDecimal(40)) {
            return AssignmentGrade.E
        }
        return AssignmentGrade.F
    }
}