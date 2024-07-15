package com.schoolwork.mgmt.server.service

import com.schoolwork.mgmt.server.constant.UserConstants
import com.schoolwork.mgmt.server.dto.SignupRequest
import com.schoolwork.mgmt.server.dto.user.ProfileUpdateRequest
import com.schoolwork.mgmt.server.dto.user.UserDto
import com.schoolwork.mgmt.server.enum.DbBoolean
import com.schoolwork.mgmt.server.error.NotFoundException
import com.schoolwork.mgmt.server.error.UnauthorizedException
import com.schoolwork.mgmt.server.error.ValidationException
import com.schoolwork.mgmt.server.model.*
import com.schoolwork.mgmt.server.repository.*
import com.schoolwork.mgmt.server.security.UserRole
import org.apache.logging.log4j.LogManager
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class UserService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val assignmentRepository: AssignmentRepository,
    private val questionRepository: QuestionRepository,
    private val studentQuestionRepository: StudentQuestionRepository,
    private val assignmentService: AssignmentService,
    private val studentAssignmentRepository: StudentAssignmentRepository,
    private val challengeProgressRepository: ChallengeProgressRepository,
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
        val user = getUserInSession() ?: throw UnauthorizedException()
        return user
    }

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun getUserByUsername(self: User, username: String): User {
        userRepository.findByUsername(username)?.let {
            if (self.mentor?.id != it.id && self.peer?.id != it.id && self.id != it.id) {
                throw ValidationException("User ${self.username} is not allowed to view $username")
            }
            return it
        } ?: run { throw NotFoundException("User $username not found.") }
    }

    fun signup(request: SignupRequest): User {
        validate(request)
        val now = LocalDateTime.now()
        val user = userRepository.save(User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname,
            role = UserRole.STUDENT,
            biography = "",
            isChallenger = DbBoolean.Y,
            createdAt = now,
            updatedAt = now,
        ))
        logger.info("User created for ${request.username}")
        createPeerAndMentor(user)
        initializeChallengeProgress(user)
        return user
    }

    fun getGroup(self: User): List<UserDto> {
        if (self.mentor == null || self.peer == null) {
            throw ValidationException("User ${self.username} does not have a mentor or peer.")
        }
        return listOf(self.mentor, self.peer, self).map { UserDto(it!!) }
    }

    fun updateProfile(self: User, request: ProfileUpdateRequest): User {
        val existingUser = getExistingUserAndValidateOwnership(self, request.username)
        existingUser.nickname = request.nickname
        existingUser.biography = request.biography
        existingUser.updatedAt = LocalDateTime.now()
        return userRepository.save(existingUser)
    }

    fun validatePassword(self: User, password: String) {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(self.username, password))
        } catch (e: AuthenticationException) {
            throw UnauthorizedException("Password does not match.")
        }
    }

    fun changePassword(self: User, newPassword: String) {
        validatePasswordFormat(newPassword)
        userRepository.save(self.also {
            it.password = passwordEncoder.encode(newPassword)
        })
    }

    private fun initializeChallengeProgress(challenger: User) {
        val now = LocalDateTime.now()
        challengeProgressRepository.save(ChallengeProgress(
            challenger = challenger,
            peerScoreOverridden = DbBoolean.N,
            mentorPasswordOverridden = DbBoolean.N,
            createdAt = now,
            updatedAt = now,
        ))
    }

    private fun createPeerAndMentor(user: User) {
        logger.info("Creating peer and mentor for ${user.username}")
        val now = LocalDateTime.now()
        val mentor = userRepository.save(User(
            username = generateRandomString(6),
            password = passwordEncoder.encode(generateRandomString(16)),
            nickname = UserConstants.USER_NAMES[Random.nextInt(0, UserConstants.USER_NAMES.size - 1)],
            role = UserRole.MENTOR,
            biography = "I’m a senior mentor at CoolCode and I hope I can help you with your journey in programing.",
            isChallenger = DbBoolean.N,
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
            biography = "I'm really interested in coding but I just can't study well...",
            isChallenger = DbBoolean.N,
            createdAt = now,
            updatedAt = now,
        ))
        logger.info("Peer ${peer.username} created for ${user.username}")
        user.mentor = mentor
        user.peer = peer
        userRepository.save(user)

        // Give poor answer for peer so the auto evaluation will give low score
        val assignments = assignmentRepository.findAll()
        assignments.forEach { assignment ->
            val questions = questionRepository.findAllByAssignment(assignment)
            questions.forEach { question ->
                studentQuestionRepository.save(StudentQuestion(
                    questionId = question.id!!,
                    studentId = peer.id!!,
                    answer = Question.Answer.A,
                    createdAt = now,
                    updatedAt = now
                ))
            }
            studentAssignmentRepository.save(StudentAssignment(
                assignment = assignment,
                student = peer,
                createdAt = now,
                updatedAt = now,
            ))
        }
        assignmentService.mentorTryingToCorrectScore()
    }

    private fun generateRandomString(length: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
    }

    private fun validate(request: SignupRequest) {
        if (userRepository.findByUsername(request.username) != null) {
            throw ValidationException("User already exists for ${request.username}")
        }
        validatePasswordFormat(request.password)
    }

    private fun getExistingUserAndValidateOwnership(user: User, username: String): User {
        if (user.username != username) {
            throw UnauthorizedException()
        }
        return userRepository.findByUsername(username) ?: throw NotFoundException("User $username does not exist.")
    }

    private fun validatePasswordFormat(password: String) {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@.#$!%*?&^])[A-Za-z\\d@.#$!%*?&]{8,15}$")
        if (!regex.matches(password)) {
            throw ValidationException("Password should contain at least one lowercase and uppercase alphabet, one number, one special character @.#$!%*?&^, and with length between 8 and 15.")
        }
    }
}