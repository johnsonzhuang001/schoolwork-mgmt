package com.schoolwork.mgmt.server.repository

import com.schoolwork.mgmt.server.model.Assignment
import com.schoolwork.mgmt.server.model.Question
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository: CrudRepository<Question, Long> {
    fun findAllByAssignment(assignment: Assignment): List<Question>
    fun deleteAllByAssignment(assignment: Assignment)
}