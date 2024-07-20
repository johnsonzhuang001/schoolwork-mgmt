package com.coolcode.server.dto.assignment

import com.coolcode.server.enum.AssignmentGrade
import com.coolcode.server.model.Question
import java.math.BigDecimal
import java.time.ZonedDateTime

data class QuestionDto(
    val id: Long,
    val description: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: Question.Answer? = null
)

data class AssignmentDto(
    val id: Long,
    val title: String,
    val level: String,
    val deadline: ZonedDateTime,
    val questionCount: Int,
    val finishCount: Int,
    val submitted: Boolean,
    val score: BigDecimal? = null,
    val grade: AssignmentGrade? = null
)

data class AssignmentWithQuestionsDto(
    val id: Long,
    val title: String,
    val level: String,
    val deadline: ZonedDateTime,
    val submitted: Boolean,
    val score: BigDecimal? = null,
    val questions: List<QuestionDto>,
)
