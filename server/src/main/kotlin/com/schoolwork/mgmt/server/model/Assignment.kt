package com.schoolwork.mgmt.server.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "assignment")
class Assignment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assignment_seq")
    @SequenceGenerator(name = "assignment_seq", sequenceName = "assignment_seq", allocationSize = 1)
    val id: Long? = null,
    var title: String,
    var createdAt: LocalDateTime,
) {
    constructor(): this(
        title  = "",
        createdAt = LocalDateTime.now(),
    )
}
