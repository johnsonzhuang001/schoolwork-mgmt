package com.schoolwork.mgmt.server.dto.user

import com.schoolwork.mgmt.server.model.User
import com.schoolwork.mgmt.server.security.UserRole

data class UserDto(
    val username: String,
    val nickname: String,
    val role: UserRole,
    val biography: String?
) {
    constructor(user: User): this(
        username = user.username,
        nickname = user.nickname,
        role = user.role,
        biography = user.biography
    )
}
