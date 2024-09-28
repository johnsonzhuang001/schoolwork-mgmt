package com.coolcode.server.dto.evaluation

data class EvaluationResult(
    val runId: String,
    val score: Int,
    val message: String
)
