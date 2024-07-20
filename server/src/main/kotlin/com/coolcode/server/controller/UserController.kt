package com.coolcode.server.controller

import com.coolcode.server.dto.OkDto
import com.coolcode.server.dto.user.ChangePasswordRequest
import com.coolcode.server.dto.user.ProfileUpdateRequest
import com.coolcode.server.dto.user.UserDto
import com.coolcode.server.dto.user.ValidatePasswordRequest
import com.coolcode.server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
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

    @Transactional
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

    @PostMapping("/password/validate")
    fun validatePassword(@RequestBody request: ValidatePasswordRequest): ResponseEntity<OkDto> {
        val self = userService.requireUserInSession()
        userService.validatePassword(self, request.password)
        return ResponseEntity(OkDto(), HttpStatus.OK)
    }

    @Transactional
    @PostMapping("/password/change")
    fun validatePassword(@RequestBody request: ChangePasswordRequest): ResponseEntity<OkDto> {
        val self = userService.requireUserInSession()
        userService.changePassword(self, request.password)
        return ResponseEntity(OkDto(), HttpStatus.OK)
    }
}