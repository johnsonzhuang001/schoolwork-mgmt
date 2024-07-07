package com.schoolwork.mgmt.server.repository

import com.schoolwork.mgmt.server.model.StudentQuestion
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentQuestionRepository: CrudRepository<StudentQuestion, Long> {
    fun findAllByStudentIdAndQuestionIdIn(studentId: Long, questionIds: List<Long>): List<StudentQuestion>
    fun findByStudentIdAndQuestionId(studentId: Long, questionId: Long): StudentQuestion?
}