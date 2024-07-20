package com.coolcode.server.dto

data class SignupRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val discordUserId: Long,
)