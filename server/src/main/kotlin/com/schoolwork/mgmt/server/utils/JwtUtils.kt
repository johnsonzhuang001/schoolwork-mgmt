package com.schoolwork.mgmt.server.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {
    companion object {
        private const val SECRET = "no_one_will_find_this_secret"
        private const val EXPIRATION_TIME = 864000000 // 10 days
        private const val TOKEN_HEADER = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
    }

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact()
    }

    fun extractUsername(token: String): String {
        return Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(TOKEN_HEADER)
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length)
        }
        return null
    }
}