package com.schoolwork.mgmt.server.controller

import com.schoolwork.mgmt.server.dto.user.SelfDto
import com.schoolwork.mgmt.server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/self")
    fun getSelf(): ResponseEntity<SelfDto?> {
        val user = userService.requireUserInSession()
        return ResponseEntity(SelfDto(user), HttpStatus.OK)
    }
}