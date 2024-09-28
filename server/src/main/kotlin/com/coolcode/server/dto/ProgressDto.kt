package com.coolcode.server.dto

import java.time.ZonedDateTime

data class ProgressDto(
    val score: Int,
    var isPeerScoreOverridden: Boolean,
    val isMentorPasswordOverridden: Boolean,
    val isPeerAllScoresOverridden: Boolean,
    val startedAt: ZonedDateTime,
)
