package com.coolcode.server.dto.evaluation

import java.math.BigDecimal

data class EvaluationResult(
    val runId: String,
    val score: BigDecimal,
    val message: String
)
