package com.schoolwork.mgmt.server.repository

import com.schoolwork.mgmt.server.model.StudentAssignment
import com.schoolwork.mgmt.server.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentAssignmentRepository: CrudRepository<StudentAssignment, Long> {
    fun findByStudentIdAndAssignmentId(studentId: Long, assignmentId: Long): StudentAssignment?
    fun findAllByStudent(student: User): List<StudentAssignment>
}