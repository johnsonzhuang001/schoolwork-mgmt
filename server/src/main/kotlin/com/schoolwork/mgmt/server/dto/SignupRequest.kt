package com.schoolwork.mgmt.server.dto

data class SignupRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val discordUserId: Long,
)