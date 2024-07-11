package com.schoolwork.mgmt.server.dto.assignment

import com.schoolwork.mgmt.server.model.Question

data class SubmitQuestionRequest(
    val id: Long,
    val answer: Question.Answer?
)

data class SubmitAssignmentRequest(
    val id: Long,
    val questions: List<SubmitQuestionRequest>,
)
