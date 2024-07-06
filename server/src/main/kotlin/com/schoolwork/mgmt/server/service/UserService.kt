package com.schoolwork.mgmt.server.service

import com.schoolwork.mgmt.server.dto.SignupRequest
import com.schoolwork.mgmt.server.error.NotFoundException
import com.schoolwork.mgmt.server.error.UnauthorizedException
import com.schoolwork.mgmt.server.error.UserNotInSessionException
import com.schoolwork.mgmt.server.model.User
import com.schoolwork.mgmt.server.repository.UserRepository
import com.schoolwork.mgmt.server.security.UserRole
import org.apache.logging.log4j.LogManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    companion object {
        private val logger = LogManager.getLogger()
    }

    fun getUserInSession(): User? {
        val auth = SecurityContextHolder.getContext().authentication
        val username = auth.name
        return getUserByUsername(username)
    }

    fun requireUserInSession(): User {
        val user = getUserInSession() ?: throw UserNotInSessionException()
        return user
    }

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun signup(request: SignupRequest): User {
        validate(request)
        val now = LocalDateTime.now()
        val user = userRepository.save(User(
            id = null,
            password = passwordEncoder.encode(request.password),
            username = request.username,
            nickname = request.nickname,
            role = UserRole.STUDENT,
            createdAt = now,
            updatedAt = now,
            deletedAt = null
        ))
        logger.info("User created for ${request.username}")
        return user
    }

    private fun validate(request: SignupRequest) {
        if (userRepository.findByUsername(request.username) != null) {
            throw IllegalArgumentException("User already exists for ${request.username}")
        }
    }

    private fun getExistingUserAndValidateOwnership(user: User, id: Long): User {
        if (user.getId() != id) {
            throw UnauthorizedException()
        }
        val existingUserOptional = userRepository.findById(id)
        if (existingUserOptional.isEmpty) throw NotFoundException("User with id $id does not exist.")
        return existingUserOptional.get()
    }
}