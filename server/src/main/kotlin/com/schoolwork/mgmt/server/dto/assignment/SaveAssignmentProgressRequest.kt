package com.schoolwork.mgmt.server.dto.assignment

data class SaveAssignmentProgressRequest(
    val id: Long,
    val questions: List<SubmitQuestionRequest>,
)
