package com.schoolwork.mgmt.server.controller

import com.schoolwork.mgmt.server.dto.JwtRequest
import com.schoolwork.mgmt.server.dto.OkDto
import com.schoolwork.mgmt.server.dto.SignInRequest
import com.schoolwork.mgmt.server.dto.SignupRequest
import com.schoolwork.mgmt.server.error.HttpException
import com.schoolwork.mgmt.server.service.UserService
import com.schoolwork.mgmt.server.utils.AuthUtils
import com.schoolwork.mgmt.server.utils.JwtUtils
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtUtils: JwtUtils,
    private val authUtils: AuthUtils
) {

    companion object {
        private val logger = LogManager.getLogger()
    }

    @PostMapping("/jwt")
    fun jwt(@RequestBody request: SignInRequest): ResponseEntity<String> {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))
        return ResponseEntity(jwtUtils.generateToken(authentication.name), HttpStatus.OK)
    }

    @Transactional
    @PostMapping("/signup")
    fun signup(@RequestBody request: SignupRequest): ResponseEntity<String> {
        logger.info("Received sign up request for ${request.username}")
        if (hasLoggedIn()) {
            logger.error("Failed to sign up: ${request.username} are already logged in")
            throw HttpException(HttpStatus.BAD_REQUEST, "You are already logged in")
        }
        try {
            val user = userService.signup(request)
            val accessToken = authUtils.generateToken(user.username)
            logger.info("Signed up successfully for ${request.username}")
            return ResponseEntity(accessToken, HttpStatus.OK)
        } catch (e: Exception) {
            logger.error("Failed to sign up: ${e.message}")
            throw HttpException(HttpStatus.BAD_REQUEST, e.message ?: "Failed to sign up. Please contact support")
        }
    }

    @PostMapping("/jwt/validate")
    fun validateJwt(@RequestBody request: JwtRequest): ResponseEntity<Boolean> {
        try {
            if (request.accessToken == null) return ResponseEntity(false, HttpStatus.OK)
            val username = jwtUtils.extractUsername(request.accessToken)
            userService.getUserByUsername(username) ?: return ResponseEntity(false, HttpStatus.OK)
            return ResponseEntity(true, HttpStatus.OK)
        } catch (e: Exception) {
            logger.error("Failed to validate JWT: ${e.message}")
            return ResponseEntity(false, HttpStatus.OK)
        }
    }

    @PostMapping("/jwt/refresh")
    fun refreshToken(@RequestBody request: JwtRequest): ResponseEntity<String> {
        if (request.accessToken == null) throw HttpException(HttpStatus.BAD_REQUEST, "JWT is not provided")
        val username = try {
            jwtUtils.extractUsername(request.accessToken)
        } catch (err: Exception) {
            logger.error("Failed to validate JWT: ${err.message}")
            throw HttpException(HttpStatus.BAD_REQUEST, "Failed to validate JWT")
        }
        userService.getUserByUsername(username) ?: throw HttpException(HttpStatus.BAD_REQUEST, "User cannot be found with the provided token")
        logger.info("Token refreshed for $username")
        return ResponseEntity(jwtUtils.generateToken(username), HttpStatus.OK)
    }

    @PostMapping("/signin")
    fun signin(@RequestBody request: SignInRequest): ResponseEntity<OkDto> {
        try {
            logger.info("Received sign in request for ${request.username}")
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))
            logger.info("Signed in successfully for ${request.username}")
            return ResponseEntity(OkDto(), HttpStatus.OK)
        } catch (e: AuthenticationException) {
            throw HttpException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect")
        } catch (e: Exception) {
            logger.error("Failed to sign in: ${e.message}")
            throw HttpException(HttpStatus.UNAUTHORIZED, "Failed to sign in. Please contact support")
        }
    }

    @GetMapping("/impersonate/{username}") // For testing only
    fun impersonate(@PathVariable username: String): ResponseEntity<String> {
        userService.getUserByUsername(username) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val accessToken = authUtils.generateToken(username)
        return ResponseEntity(accessToken, HttpStatus.OK)
    }

    private fun hasLoggedIn(): Boolean {
        val auth = SecurityContextHolder.getContext().authentication
        return auth.isAuthenticated && auth.name != "anonymousUser"
    }
}