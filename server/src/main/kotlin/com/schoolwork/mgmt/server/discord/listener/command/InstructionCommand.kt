package com.schoolwork.mgmt.server.discord.listener.command

import com.schoolwork.mgmt.server.service.AssignmentService
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class InstructionCommand(
    private val assignmentService: AssignmentService,
): Command() {
    final override fun getName() = "coolcodehacking_instruction"
    final override fun execute(event: ChatInputInteractionEvent): Mono<Void> {
        val author = event.interaction.user
        val discordUserId = author.id.asLong()
        return event.reply(assignmentService.getInstruction(discordUserId))
            .withEphemeral(true)
    }
}