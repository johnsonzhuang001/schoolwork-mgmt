package com.schoolwork.mgmt.server.repository

import com.schoolwork.mgmt.server.model.StudentAssignment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentAssignmentRepository: CrudRepository<StudentAssignment, Long> {
    fun findByStudentIdAndAssignmentId(studentId: Long, assignmentId: Long): StudentAssignment?
}