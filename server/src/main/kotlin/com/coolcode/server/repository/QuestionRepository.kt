package com.coolcode.server.repository

import com.coolcode.server.model.Assignment
import com.coolcode.server.model.Question
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository: CrudRepository<Question, Long> {
    fun findAllByAssignment(assignment: Assignment): List<Question>
    fun countByAssignment(assignment: Assignment): Int
    fun deleteAllByAssignment(assignment: Assignment)
}