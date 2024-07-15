package com.schoolwork.mgmt.server.model

import com.schoolwork.mgmt.server.enum.DbBoolean
import com.schoolwork.mgmt.server.security.UserRole
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    val id: Long? = null,
    var username: String,
    var password: String, // Encrypted
    var nickname: String,
    @Enumerated(EnumType.STRING)
    var role: UserRole,
    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = true)
    var mentor: User? = null,
    @OneToOne
    @JoinColumn(name = "peer_id", nullable = true)
    var peer: User? = null,
    var biography: String,
    @Enumerated(EnumType.STRING)
    val isChallenger: DbBoolean,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var deletedAt: LocalDateTime? = null
) {
    constructor(): this(
        username  = "",
        password = "",
        nickname = "",
        biography = "",
        role = UserRole.STUDENT,
        isChallenger = DbBoolean.N,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )

    fun isChallenger() = isChallenger == DbBoolean.Y
}
