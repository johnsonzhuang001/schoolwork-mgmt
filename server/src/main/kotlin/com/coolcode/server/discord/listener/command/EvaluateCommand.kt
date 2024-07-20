package com.coolcode.server.discord.listener.command

import com.coolcode.server.service.EvaluationService
import com.coolcode.server.service.UserService
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class EvaluateCommand(
    private val userService: UserService,
    private val evaluationService: EvaluationService,
): Command() {
    final override fun getName() = "coolcodehacking_evaluate"
    final override fun execute(event: ChatInputInteractionEvent): Mono<Void> {
        val author = event.interaction.user
        val discordUserId = author.id.asLong()
        val user = userService.getUserByDiscordId(discordUserId) ?: run {
            return event.reply("Please use the start command to get your student account.")
                .withEphemeral(true)
        }
        return event.reply("Your score is ${evaluationService.evaluate(user.username)}")
            .withEphemeral(true)
    }
}