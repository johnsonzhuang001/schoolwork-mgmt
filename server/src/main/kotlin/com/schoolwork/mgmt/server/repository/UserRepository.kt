package com.schoolwork.mgmt.server.repository

import com.schoolwork.mgmt.server.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}