package com.coolcode.server.discord.listener.command

import com.coolcode.server.service.EvaluationService
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProgressCommand(
    private val evaluationService: EvaluationService,
): Command() {
    final override fun getName() = "coolcodehacking_progress"
    final override fun execute(event: ChatInputInteractionEvent): Mono<Void> {
        val author = event.interaction.user
        val discordUserId = author.id.asLong()
        return event.reply(evaluationService.getProgress(discordUserId))
            .withEphemeral(true)
    }
}