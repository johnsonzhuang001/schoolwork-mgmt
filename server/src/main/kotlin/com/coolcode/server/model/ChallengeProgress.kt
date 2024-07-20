package com.coolcode.server.model

import com.coolcode.server.enum.DbBoolean
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "challenge_progress")
class ChallengeProgress(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "challenge_progress_seq")
    @SequenceGenerator(name = "challenge_progress_seq", sequenceName = "challenge_progress_seq", allocationSize = 1)
    val id: Long? = null,
    @OneToOne
    @JoinColumn(name = "challenger_id", nullable = false)
    val challenger: User,
    @Enumerated(EnumType.STRING)
    var peerScoreOverridden: DbBoolean,
    @Enumerated(EnumType.STRING)
    var mentorPasswordOverridden: DbBoolean,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
) {
    constructor(): this(
        challenger = User(),
        peerScoreOverridden = DbBoolean.N,
        mentorPasswordOverridden = DbBoolean.N,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )

    fun isPeerScoreOverriden() = peerScoreOverridden == DbBoolean.Y
    fun isMentorPasswordOverridden() = mentorPasswordOverridden == DbBoolean.Y
}
