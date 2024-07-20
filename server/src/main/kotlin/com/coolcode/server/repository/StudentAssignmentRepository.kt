package com.coolcode.server.repository

import com.coolcode.server.model.StudentAssignment
import com.coolcode.server.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentAssignmentRepository: CrudRepository<StudentAssignment, Long> {
    fun findByStudentIdAndAssignmentId(studentId: Long, assignmentId: Long): StudentAssignment?
    fun findAllByStudent(student: User): List<StudentAssignment>
}