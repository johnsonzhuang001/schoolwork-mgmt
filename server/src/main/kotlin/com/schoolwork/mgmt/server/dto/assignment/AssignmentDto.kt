package com.schoolwork.mgmt.server.dto.assignment

import com.schoolwork.mgmt.server.enum.AssignmentGrade
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class QuestionDto(
    val id: Long,
    val description: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
)

data class AssignmentDto(
    val id: Long,
    val title: String,
    val level: String,
    val deadline: ZonedDateTime,
    val questionCount: Int,
    val finishCount: Int,
    val score: BigDecimal? = null,
    val grade: AssignmentGrade? = null
)

data class AssignmentWithQuestionsDto(
    val id: Long,
    val title: String,
    val questions: List<QuestionDto>,
)
