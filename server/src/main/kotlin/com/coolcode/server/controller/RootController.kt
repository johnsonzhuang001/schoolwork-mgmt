package com.coolcode.server.controller

import com.coolcode.server.dto.evaluation.EvaluationRequest
import com.coolcode.server.dto.evaluation.TeamResponse
import com.coolcode.server.service.EvaluationService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class RootController(
    @Value("\${app.instruction-url}")
    private val instructionUrl: String,
    private val evaluationService: EvaluationService,
) {
    @PostMapping("/evaluate")
    fun evaluate(@RequestBody request: EvaluationRequest) {
        evaluationService.startEvaluation(request)
    }

    @GetMapping("/")
    fun readme(): ResponseEntity<Void> {
        val headers = HttpHeaders()
        headers.setLocation(URI.create(instructionUrl))
        return ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY)
    }

    // Test endpoint for evaluation
    @PostMapping("/coolcodehack")
    fun solution(): ResponseEntity<TeamResponse> {
        return ResponseEntity(TeamResponse(
            username = "johnson.zhuang",
            password = "lG5xlhd&6o6o",
        ), HttpStatus.OK)
    }
}