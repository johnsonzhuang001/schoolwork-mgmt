package com.schoolwork.mgmt.server.service

import com.schoolwork.mgmt.server.constant.UserConstants
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
import kotlin.random.Random

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
            username = request.username,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname,
            role = UserRole.STUDENT,
            createdAt = now,
            updatedAt = now,
        ))
        logger.info("User created for ${request.username}")
        createPeerAndMentor(user)
        return user
    }

    private fun createPeerAndMentor(user: User) {
        logger.info("Creating peer and mentor for ${user.username}")
        val now = LocalDateTime.now()
        val mentor = userRepository.save(User(
            username = generateRandomString(6),
            password = passwordEncoder.encode(generateRandomString(16)),
            nickname = UserConstants.USER_NAMES[Random.nextInt(0, UserConstants.USER_NAMES.size - 1)],
            role = UserRole.MENTOR,
            createdAt = now,
            updatedAt = now,
        ))
        logger.info("Mentor ${mentor.username} created for ${user.username}")
        val peer = userRepository.save(User(
            username = generateRandomString(6),
            password = passwordEncoder.encode(generateRandomString(16)),
            nickname = UserConstants.USER_NAMES[Random.nextInt(0, UserConstants.USER_NAMES.size - 1)],
            role = UserRole.STUDENT,
            peer = user,
            mentor = mentor,
            createdAt = now,
            updatedAt = now,
        ))
        logger.info("Peer ${peer.username} created for ${user.username}")
        user.mentor = mentor
        user.peer = peer
        userRepository.save(user)
    }

    private fun generateRandomString(length: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
    }

    private fun validate(request: SignupRequest) {
        if (userRepository.findByUsername(request.username) != null) {
            throw IllegalArgumentException("User already exists for ${request.username}")
        }
    }

    private fun getExistingUserAndValidateOwnership(user: User, id: Long): User {
        if (user.id != id) {
            throw UnauthorizedException()
        }
        val existingUserOptional = userRepository.findById(id)
        if (existingUserOptional.isEmpty) throw NotFoundException("User with id $id does not exist.")
        return existingUserOptional.get()
    }
}