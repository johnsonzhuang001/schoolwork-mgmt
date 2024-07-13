package com.schoolwork.mgmt.server.controller

import com.schoolwork.mgmt.server.dto.user.ProfileUpdateRequest
import com.schoolwork.mgmt.server.dto.user.UserDto
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
    fun getSelf(): ResponseEntity<UserDto> {
        val user = userService.requireUserInSession()
        return ResponseEntity(UserDto(user), HttpStatus.OK)
    }

    @GetMapping("/username/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<UserDto?> {
        val self = userService.requireUserInSession()
        return ResponseEntity(UserDto(userService.getUserByUsername(self, username)), HttpStatus.OK)
    }

    @PostMapping("/profile")
    fun amendUser(@RequestBody request: ProfileUpdateRequest): ResponseEntity<UserDto> {
        val self = userService.requireUserInSession()
        val updatedUser = userService.updateProfile(self, request)
        return ResponseEntity(UserDto(updatedUser), HttpStatus.OK)
    }

    @GetMapping("/group")
    fun getGroup(): ResponseEntity<List<UserDto>> {
        val self = userService.requireUserInSession()
        return ResponseEntity(userService.getGroup(self), HttpStatus.OK)
    }
}