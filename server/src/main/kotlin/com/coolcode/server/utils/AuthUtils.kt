package com.coolcode.server.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.coolcode.server.error.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and

data class AuthToken(
    val username: String,
    val hash: String,
)

@Component
class AuthUtils(
    private val objectMapper: ObjectMapper
) {
    companion object {
        private const val TOKEN_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
        private const val SALT = "coolcodehacker"
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(TOKEN_HEADER)
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length)
        }
        return null
    }

    fun base64Decode(token: String): AuthToken {
        val decoded = String(Base64.getDecoder().decode(token))
        return objectMapper.readValue(decoded, AuthToken::class.java)
    }

    fun validateAuthToken(token: AuthToken) {
        // Compare the hash from the token with the hashed username + salt
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest((token.username + SALT).toByteArray())
            .joinToString("") { "%02x".format(it and 0xFF.toByte()) }
        if (hash != token.hash) {
            throw UnauthorizedException("Invalid username or password")
        }
    }

    fun generateToken(username: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(username.toByteArray())
            .joinToString("") { "%02x".format(it and 0xFF.toByte()) }
        return String(Base64.getEncoder().encode(objectMapper.writeValueAsBytes(AuthToken(
            username = username,
            hash = hash
        ))))
    }
}