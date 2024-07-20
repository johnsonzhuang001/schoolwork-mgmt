package com.coolcode.server.model

import com.coolcode.server.enum.DbBoolean
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "student_question")
class StudentQuestion(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_question_seq")
    @SequenceGenerator(name = "student_question_seq", sequenceName = "student_question_seq", allocationSize = 1)
    val id: Long? = null,
    val questionId: Long,
    val studentId: Long,
    @Enumerated(EnumType.STRING)
    var answer: Question.Answer,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
) {
    constructor(): this(
        questionId = -1,
        studentId = -1,
        answer = Question.Answer.A,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )
}