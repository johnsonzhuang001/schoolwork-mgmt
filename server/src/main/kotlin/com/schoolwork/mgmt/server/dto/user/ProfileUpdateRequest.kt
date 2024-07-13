package com.schoolwork.mgmt.server.dto.user

data class ProfileUpdateRequest(
    val username: String,
    val nickname: String,
    val biography: String
)
