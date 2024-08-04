package com.coolcode.server.dto

import java.math.BigDecimal

data class ProgressDto(
    val score: BigDecimal,
    var isPeerScoreOverridden: Boolean,
    val isMentorPasswordOverridden: Boolean,
    val isPeerAllScoresOverridden: Boolean,
)
