package com.coolcode.server.service

import com.coolcode.server.dto.ProgressDto
import com.coolcode.server.error.NotFoundException
import com.coolcode.server.model.User
import com.coolcode.server.repository.ChallengeProgressRepository
import org.springframework.stereotype.Service
import java.time.ZoneOffset

@Service
class InstructionService(
    private val challengeProgressRepository: ChallengeProgressRepository,
    private val evaluationService: EvaluationService,
) {
    fun getProgress(user: User): ProgressDto {
        challengeProgressRepository.findByChallenger(user)?.let {
            return ProgressDto(
                isPeerScoreOverridden = it.isPeerScoreOverriden(),
                isPeerAllScoresOverridden = evaluationService.isAllScoreOverridden(user.peer!!),
                isMentorPasswordOverridden = it.isMentorPasswordOverridden(),
                score = evaluationService.evaluate(user.username),
                startedAt = it.createdAt.atZone(ZoneOffset.UTC),
            )
        } ?: throw NotFoundException("User ${user.username} is not a challenger.")
    }
}