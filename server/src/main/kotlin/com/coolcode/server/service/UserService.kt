package com.coolcode.server.service

import com.coolcode.server.constant.UserConstants
import com.coolcode.server.dto.SignupRequest
import com.coolcode.server.dto.user.ProfileUpdateRequest
import com.coolcode.server.dto.user.UserDto
import com.coolcode.server.enum.DbBoolean
import com.coolcode.server.error.NotFoundException
import com.coolcode.server.error.UnauthorizedException
import com.coolcode.server.error.ValidationException
import com.coolcode.server.model.*
import com.coolcode.server.repository.*
import com.coolcode.server.security.UserRole
import com.coolcode.server.utils.PasswordUtils
import discord4j.common.util.Snowflake
import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.channel.PrivateChannel
import discord4j.core.util.EntityUtil
import discord4j.discordjson.json.DMCreateRequest
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
//    private val discordClient: GatewayDiscordClient,
) {

    companion object {
        private val logger = LogManager.getLogger()
    }

    fun getUserByDiscordId(discordId: Long): User? = userRepository.findByDiscordUserId(discordId)

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
            discordUserId = request.discordUserId,
            createdAt = now,
            updatedAt = now,
        ))
        logger.info("User created for ${request.username}")
        initializeChallengeProgress(user)
        createPeerAndMentor(user)
        return user
    }

    fun getGroup(self: User): List<UserDto> {
        if (self.mentor == null || self.peer == null) {
            throw ValidationException("User ${self.username} does not have a mentor or peer.")
        }
        return listOf(self.mentor, self.peer, self).map { UserDto(it!!) }
    }

    fun updateProfile(self: User, request: ProfileUpdateRequest): User {
        validateNickNameFormat(request.nickname)
        validateBiographyFormat(request.biography)
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
        val now = LocalDateTime.now()
        userRepository.save(self.also {
            it.password = passwordEncoder.encode(newPassword)
            it.updatedAt = now
        })
        if (self.role == UserRole.MENTOR) {
            userRepository.findByMentorAndIsChallenger(self, DbBoolean.Y)?.also { challenger ->
                val progress = challengeProgressRepository.findByChallenger(challenger)
                    ?: throw NotFoundException("Challenge progress is not initiated for ${challenger.username}.")
                progress.mentorPasswordOverridden = DbBoolean.Y
                progress.updatedAt = now
                challengeProgressRepository.save(progress)
//                discordClient.restClient.userService
//                    .createDM(
//                        DMCreateRequest
//                            .builder()
//                            .recipientId(Snowflake.asString(challenger.discordUserId!!))
//                            .build()
//                    ).map {
//                        EntityUtil.getChannel(discordClient, it)
//                    }.cast(PrivateChannel::class.java).flatMap {
//                        it.createMessage(assignmentService.getInstruction(progress))
//                    }.block()
            } ?: run {
                throw NotFoundException("There is no challenger under mentor ${self.username}.")
            }
        }
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
            password = passwordEncoder.encode(PasswordUtils.generateRandomPassword(12)),
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
            password = passwordEncoder.encode(PasswordUtils.generateRandomPassword(12)),
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
        validateUsernameFormat(request.username)
        validatePasswordFormat(request.password)
        if (userRepository.findByUsername(request.username) != null) {
            throw ValidationException("User already exists for ${request.username}")
        }
    }

    private fun getExistingUserAndValidateOwnership(user: User, username: String): User {
        if (user.username != username) {
            throw UnauthorizedException()
        }
        return userRepository.findByUsername(username) ?: throw NotFoundException("User $username does not exist.")
    }

    private fun validateUsernameFormat(username: String) {
        if (username.length < 6 || username.length > 16) {
            throw ValidationException("Username should be 6 to 16 characters long.")
        }
    }

    private fun validatePasswordFormat(password: String) {
        if (!PasswordUtils.isValidPassword(password)) {
            throw ValidationException("Password should contain at least one lowercase and uppercase alphabet, one number, one special character @.#$!%*?&^, and with length between 8 and 15.")
        }
    }

    private fun validateNickNameFormat(nickname: String) {
        if (nickname.length > 16) {
            throw ValidationException("Nick name should not exceed 16 characters.")
        }
    }

    private fun validateBiographyFormat(biography: String) {
        if (biography.length > 500) {
            throw ValidationException("Biography should not exceed 500 characters.")
        }
    }
}