package com.schoolwork.mgmt.server.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.schoolwork.mgmt.server.utils.AuthUtils
import com.schoolwork.mgmt.server.utils.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class Sha512AuthenticationFilter(
    private val authUtils: AuthUtils,
    private val objectMapper: ObjectMapper,
    private val userDetailsService: UserDetailsService,
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = authUtils.resolveToken(request) ?: return filterChain.doFilter(request, response)
            val authToken = authUtils.base64Decode(accessToken)
            authUtils.validateAuthToken(authToken)
            val username = authToken.username
            val user = userDetailsService.loadUserByUsername(username)
            val authentication = UsernamePasswordAuthenticationToken(user.username, user.password, user.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            val errorDetails = HashMap<String, String>()
            errorDetails["message"] = "Authentication Error: ${e.message}"
            response.status = HttpStatus.FORBIDDEN.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            objectMapper.writeValue(response.writer, errorDetails)
        }
        filterChain.doFilter(request, response)
    }
}