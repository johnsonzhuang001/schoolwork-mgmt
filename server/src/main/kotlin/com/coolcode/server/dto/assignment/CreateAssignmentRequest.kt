package com.coolcode.server.dto.assignment

import com.coolcode.server.enum.AssignmentLevel
import com.coolcode.server.model.Question
import java.time.LocalDateTime

data class CreateQuestionRequest(
    val description: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: Question.Answer
)

data class CreateAssignmentRequest(
    val title: String,
    val level: AssignmentLevel,
    val deadline: LocalDateTime,
    val questions: List<CreateQuestionRequest>,
)
