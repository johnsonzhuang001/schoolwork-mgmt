package com.coolcode.server.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "student_assignment")
class StudentAssignment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_assignment_seq")
    @SequenceGenerator(name = "student_assignment_seq", sequenceName = "student_assignment_seq", allocationSize = 1)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    val assignment: Assignment,
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    val student: User,
    var score: BigDecimal? = null,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
) {
    constructor(): this(
        assignment = Assignment(),
        student = User(),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )
}