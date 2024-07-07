package com.schoolwork.mgmt.server.dto.assignment

import com.schoolwork.mgmt.server.model.Question

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
    val questions: List<CreateQuestionRequest>,
)
