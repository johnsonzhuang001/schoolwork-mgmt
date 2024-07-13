package com.schoolwork.mgmt.server.dto.user

import com.schoolwork.mgmt.server.security.UserRole

data class UserDto(
    val username: String,
    val nickname: String,
    val role: UserRole,
    val biography: String?
)
