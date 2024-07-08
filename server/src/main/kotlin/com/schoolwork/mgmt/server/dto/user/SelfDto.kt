package com.schoolwork.mgmt.server.dto.user

import com.schoolwork.mgmt.server.model.User
import com.schoolwork.mgmt.server.security.UserRole

data class SelfDto(
    val id: Long?,
    val username: String,
    val nickname: String?,
    val role: UserRole,
) {
    constructor(user: User): this(
        id = user.id,
        username = user.username,
        nickname = user.nickname,
        role = user.role,
    )
}