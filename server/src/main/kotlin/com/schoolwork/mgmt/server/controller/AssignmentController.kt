package com.schoolwork.mgmt.server.controller

import com.schoolwork.mgmt.server.dto.OkDto
import com.schoolwork.mgmt.server.dto.assignment.*
import com.schoolwork.mgmt.server.service.AssignmentService
import com.schoolwork.mgmt.server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/assignment")
class AssignmentController(
    private val userService: UserService,
    private val assignmentService: AssignmentService
) {
    @GetMapping("/all")
    fun getAll() = ResponseEntity(assignmentService.getAllAssignments(userService.requireUserInSession()), HttpStatus.OK)

    @GetMapping("/details/{id}")
    fun getAssignment(@PathVariable("id") id: Long) =
        ResponseEntity(assignmentService.getAssignment(userService.requireUserInSession(), id), HttpStatus.OK)

    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('WRITE_ASSIGNMENT')")
    fun createAssignment(@RequestBody request: CreateAssignmentRequest): ResponseEntity<OkDto> {
        assignmentService.createAssignment(request)
        return ResponseEntity(OkDto(), HttpStatus.OK)
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('WRITE_ASSIGNMENT')")
    fun deleteAssignment(@PathVariable("id") id: Long): ResponseEntity<OkDto> {
        assignmentService.deleteAssignment(id)
        return ResponseEntity(OkDto(), HttpStatus.OK)
    }

    @Transactional
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('SUBMIT_ASSIGNMENT')")
    fun saveProgress(@RequestBody request: SubmitAssignmentRequest): ResponseEntity<OkDto> {
        val self = userService.requireUserInSession()
        assignmentService.saveAssignmentProgress(self, request)
        return ResponseEntity(OkDto(), HttpStatus.OK)
    }

    @Transactional
    @PostMapping("/submit")
    @PreAuthorize("hasAuthority('SUBMIT_ASSIGNMENT')")
    fun submitAssignment(@RequestBody request: SubmitAssignmentRequest): ResponseEntity<OkDto> {
        val self = userService.requireUserInSession()
        assignmentService.submitAssignment(self, request)
        return ResponseEntity(OkDto(), HttpStatus.OK)
    }

    @Transactional
    @PostMapping("/withdraw/{id}")
    @PreAuthorize("hasAuthority('SUBMIT_ASSIGNMENT')")
    fun withdrawSubmission(@PathVariable id: Long): ResponseEntity<OkDto> {
        val self = userService.requireUserInSession()
        assignmentService.withdrawSubmission(self, id)
        return ResponseEntity(OkDto(), HttpStatus.OK)
    }

    @Transactional
    @PostMapping("/score")
    @PreAuthorize("hasAuthority('SCORE_ASSIGNMENT')")
    fun scoreAssignment(@RequestBody request: UploadScoreRequest): ResponseEntity<OkDto> {
        val self = userService.requireUserInSession()
        assignmentService.uploadScore(self, request)
        return ResponseEntity(OkDto(), HttpStatus.OK)
    }
}