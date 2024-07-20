package com.coolcode.server.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "question")
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_seq")
    @SequenceGenerator(name = "question_seq", sequenceName = "question_seq", allocationSize = 1)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    val assignment: Assignment,
    val description: String,
    @Column(name = "option_a")
    val optionA: String,
    @Column(name = "option_b")
    val optionB: String,
    @Column(name = "option_c")
    val optionC: String,
    @Column(name = "option_d")
    val optionD: String,
    @Enumerated(EnumType.STRING)
    var answer: Answer,
    var createdAt: LocalDateTime,
) {
    enum class Answer {
        A, B, C, D
    }
    constructor(): this(
        assignment = Assignment(),
        description  = "",
        optionA = "",
        optionB = "",
        optionC = "",
        optionD = "",
        answer = Answer.A,
        createdAt = LocalDateTime.now(),
    )
}
