package com.schoolwork.mgmt.server.model

import com.schoolwork.mgmt.server.enum.AssignmentLevel
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "assignment")
class Assignment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assignment_seq")
    @SequenceGenerator(name = "assignment_seq", sequenceName = "assignment_seq", allocationSize = 1)
    val id: Long? = null,
    val title: String,
    @Enumerated(EnumType.STRING)
    val level: AssignmentLevel,
    val deadline: LocalDateTime,
    val createdAt: LocalDateTime,
) {
    constructor(): this(
        title  = "",
        level = AssignmentLevel.BEGINNER,
        deadline = LocalDateTime.now(),
        createdAt = LocalDateTime.now(),
    )
}
