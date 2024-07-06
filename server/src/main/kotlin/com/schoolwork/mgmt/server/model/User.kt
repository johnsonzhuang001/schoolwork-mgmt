package com.schoolwork.mgmt.server.model

import com.schoolwork.mgmt.server.security.UserRole
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private val id: Long?,
    private var username: String,
    private var password: String, // Encrypted
    private var nickname: String,
    @Enumerated(EnumType.STRING)
    private var role: UserRole,
    private var createdAt: LocalDateTime,
    private var updatedAt: LocalDateTime,
    private var deletedAt: LocalDateTime?
) {
    constructor(): this(
        id = -1,
        username  = "",
        password = "",
        nickname = "",
        role = UserRole.STUDENT,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        deletedAt = null
    )

    fun getId(): Long? = id
    fun getUsername(): String = username
    fun getPassword(): String = password
    fun getNickname(): String? = nickname
    fun getRole(): UserRole = role

    fun setPassword(password: String) {
        this.password = password
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun setNickname(nickname: String) {
        this.nickname = nickname
    }

    fun setRole(role: UserRole) {
        this.role = role
    }

    fun setUpdatedAt(updatedAt: LocalDateTime) {
        this.updatedAt = updatedAt
    }

    fun setDeletedAt(deletedAt: LocalDateTime) {
        this.deletedAt = deletedAt
    }
}
