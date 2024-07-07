package com.schoolwork.mgmt.server.repository

import com.schoolwork.mgmt.server.model.Assignment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository: CrudRepository<Assignment, Long> {
}