package com.coolcode.server.dto.assignment

import com.coolcode.server.model.Question

data class SubmitQuestionRequest(
    val id: Long,
    val answer: Question.Answer?
)

data class SubmitAssignmentRequest(
    val id: Long,
    val questions: List<SubmitQuestionRequest>,
)
