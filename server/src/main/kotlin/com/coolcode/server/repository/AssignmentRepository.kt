package com.coolcode.server.repository

import com.coolcode.server.model.Assignment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository: CrudRepository<Assignment, Long> {
}