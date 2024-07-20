package com.coolcode.server.dto.assignment

import java.math.BigDecimal

data class UploadScoreRequest(
    val username: String,
    val assignmentId: Long,
    val score: BigDecimal,
)
