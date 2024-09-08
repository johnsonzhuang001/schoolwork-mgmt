package com.coolcode.server.service

import org.apache.logging.log4j.LogManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class Scheduler(
    private val assignmentService: AssignmentService,
) {
    companion object {
        val logger = LogManager.getLogger()
    }

    @Scheduled(fixedRate = 120000) // Every 2 minutes
    fun scoreAssignments() {
        logger.info("Scheduling to update scores of assignments")
        assignmentService.mentorTryingToCorrectScore()
    }
}