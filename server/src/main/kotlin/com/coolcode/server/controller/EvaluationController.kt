package com.coolcode.server.controller

import com.coolcode.server.service.EvaluationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/evaluation")
class EvaluationController(
    private val evaluationService: EvaluationService,
) {
    @GetMapping("/username/{username}")
    fun evaluateByUsername(@PathVariable("username") username: String): ResponseEntity<Int> {
        return ResponseEntity(evaluationService.evaluate(username), HttpStatus.OK)
    }
}