package com.coolcode.server.repository

import com.coolcode.server.enum.DbBoolean
import com.coolcode.server.model.User
import com.coolcode.server.security.UserRole
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findByDiscordUserId(discordUserId: Long): User?
    fun findByUsername(username: String): User?
    fun findByUsernameAndRole(username: String, role: UserRole): User?
    fun findByMentorAndIsChallenger(mentor: User, isChallenger: DbBoolean): User?
    fun findAllByRole(role: UserRole): List<User>
    fun findAllByMentor(mentor: User): List<User>
}