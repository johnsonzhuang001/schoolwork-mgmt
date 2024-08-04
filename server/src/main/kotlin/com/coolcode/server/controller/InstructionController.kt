package com.coolcode.server.controller

import com.coolcode.server.dto.ProgressDto
import com.coolcode.server.service.InstructionService
import com.coolcode.server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/instruction")
class InstructionController(
    private val userService: UserService,
    private val instructionService: InstructionService,
) {
    @GetMapping("/progress")
    fun getProgress(): ResponseEntity<ProgressDto> {
        val user = userService.requireUserInSession()
        return ResponseEntity(instructionService.getProgress(user), HttpStatus.OK)
    }
}