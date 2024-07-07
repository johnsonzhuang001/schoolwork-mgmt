package com.schoolwork.mgmt.server.dto.assignment

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
)

data class AssignmentWithQuestionsDto(
    val id: Long,
    val title: String,
    val questions: List<QuestionDto>,
)
