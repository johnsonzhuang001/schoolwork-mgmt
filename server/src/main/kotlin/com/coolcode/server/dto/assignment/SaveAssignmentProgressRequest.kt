package com.coolcode.server.dto.assignment

data class SaveAssignmentProgressRequest(
    val id: Long,
    val questions: List<SubmitQuestionRequest>,
)
