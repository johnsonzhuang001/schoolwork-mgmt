package com.schoolwork.mgmt.server.discord.listener

import com.schoolwork.mgmt.server.discord.listener.command.Command
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ChatInputInteractionListener(
    private val commands: List<Command>,
): EventListener<ChatInputInteractionEvent>() {
    override fun getEventType() = ChatInputInteractionEvent::class
    override fun dmOnly() = false
    override fun execute(event: ChatInputInteractionEvent): Mono<Void> {
        for (command in commands) {
            if (event.commandName == command.getName()) {
                return command.execute(event)
            }
        }
        return event.reply("Please use the available commands to communicate with me.")
    }
}