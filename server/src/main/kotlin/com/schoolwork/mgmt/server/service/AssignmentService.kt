package com.schoolwork.mgmt.server.service

import com.schoolwork.mgmt.server.dto.assignment.*
import com.schoolwork.mgmt.server.error.NotFoundException
import com.schoolwork.mgmt.server.error.UnauthorizedException
import com.schoolwork.mgmt.server.model.*
import com.schoolwork.mgmt.server.repository.*
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

    fun getAllAssignments(self: User): List<AssignmentDto> = assignmentRepository.findAll().toList().map { assignment ->
        val questions = questionRepository.findAllByAssignment(assignment)
        val finishCount = studentQuestionRepository.countByStudentIdAndQuestionIdIn(self.id!!, questions.map { it.id!! })
        AssignmentDto(
            id = assignment.id!!,
            title = assignment.title,
            level = assignment.level.displayName,
            deadline = assignment.deadline.atZone(ZoneOffset.UTC),
            questionCount = questions.size,
            finishCount = finishCount
        )
    }

    fun getAssignment(assignmentId: Long): AssignmentWithQuestionsDto = assignmentRepository
        .findByIdOrNull(assignmentId)?.let { assignment ->
            AssignmentWithQuestionsDto(
                id = assignment.id!!,
                title = assignment.title,
                questions = questionRepository.findAllByAssignment(assignment).map { question ->
                    QuestionDto(
                        id = question.id!!,
                        description = question.description,
                        optionA = question.optionA,
                        optionB = question.optionB,
                        optionC = question.optionC,
                        optionD = question.optionD,
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
        assignmentRepository.findByIdOrNull(assignmentId)
            ?: throw NotFoundException("Assignment with id $assignmentId not found.")
        val existingStudentAssignment = studentAssignmentRepository
            .findByStudentIdAndAssignmentId(self.id!!, assignmentId)
            ?: throw NotFoundException("Assignment submission record is not found for ${self.username} of assignment $assignmentId.")
        studentAssignmentRepository.delete(existingStudentAssignment)
        logger.info("Successfully withdrew the submission for ${self.username} of assignment $assignmentId")
    }

    // The function for challengers to call to override score
    fun uploadScore(self: User, request: UploadScoreRequest) {
        val student = userRepository.findByUsername(request.username)
            ?: throw NotFoundException("Student with username ${request.username} not found.")
        logger.info("Uploading score for ${student.username} of assignment ${request.assignmentId}")
        if (student.mentor?.id != self.id) {
            throw UnauthorizedException("User ${self.username} is not student ${student.username}'s mentor.")
        }
        val assignment = assignmentRepository.findByIdOrNull(request.assignmentId)
            ?: throw NotFoundException("Assignment ${request.assignmentId} not found.")

        val existingStudentAssignment = studentAssignmentRepository.findByStudentIdAndAssignmentId(student.id!!, assignment.id!!)
            ?: throw NotFoundException("Student ${student.username} hasn't submitted the assignment ${assignment.id}")
        val now = LocalDateTime.now()
        existingStudentAssignment.score = request.score
        existingStudentAssignment.updatedAt = now
        studentAssignmentRepository.save(existingStudentAssignment)
        logger.info("Successfully uploaded score for ${student.username} of assignment ${request.assignmentId}")
    }

    // TODO: A schedule to call this function every 5 minutes
    fun mentorTryingToCorrectScore() {
        val studentAssignments = studentAssignmentRepository.findAll().toList()
        studentAssignments.forEach { studentAssignment ->
            // TODO: Check if student's mentor's password has been overridden. If it is overridden, fix the score
            val mentor = studentAssignment.student.mentor
            logger.info("Mentor ${mentor?.username} is trying to correct ${studentAssignment.student.username}'s score ${studentAssignment.score}")
            val score = getScore(studentAssignment.student, studentAssignment.assignment)
            studentAssignment.score = score
            studentAssignmentRepository.save(studentAssignment)
            logger.info("Mentor ${mentor?.username} successfully corrected ${studentAssignment.student.username}'s score ${studentAssignment.score}")
        }
    }

    private fun submitAssignment(self: User, request: SubmitAssignmentRequest, readyForScore: Boolean) {
        val assignment = assignmentRepository.findByIdOrNull(request.id)
            ?: throw NotFoundException("Assignment with id ${request.id} not found")
        val now = LocalDateTime.now()
        // Submit question answers
        request.questions.forEach { question ->
            val existingStudentQuestion = studentQuestionRepository.findByStudentIdAndQuestionId(self.id!!, question.id)
            if (existingStudentQuestion != null) {
                existingStudentQuestion.answer = question.answer
                existingStudentQuestion.updatedAt = now
                studentQuestionRepository.save(existingStudentQuestion)
            } else {
                studentQuestionRepository.save(StudentQuestion(
                    studentId = self.id,
                    questionId = question.id,
                    answer = question.answer,
                    createdAt = now,
                    updatedAt = now,
                ))
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

    private fun getScore(student: User, assignment: Assignment): BigDecimal {
        val questions = questionRepository.findAllByAssignment(assignment)
        val studentQuestionsById = studentQuestionRepository
            .findAllByStudentIdAndQuestionIdIn(student.id!!, questions.map { it.id!! })
            .associateBy { it.id!! }
        var correctCount = 0
        questions.forEach { question ->
            if (studentQuestionsById.containsKey(question.id!!)) {
                val studentQuestion = studentQuestionsById[question.id]!!
                if (studentQuestion.answer == question.answer) {
                    correctCount++
                }
            }
        }
        return BigDecimal(correctCount).divide(BigDecimal(questions.size)).setScale(2, RoundingMode.HALF_UP)
    }
}