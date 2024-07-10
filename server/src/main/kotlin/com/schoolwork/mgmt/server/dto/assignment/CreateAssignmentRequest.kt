package com.schoolwork.mgmt.server.dto.assignment

import com.schoolwork.mgmt.server.enum.AssignmentLevel
import com.schoolwork.mgmt.server.model.Question
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
