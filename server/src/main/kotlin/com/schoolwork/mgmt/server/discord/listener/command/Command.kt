package com.schoolwork.mgmt.server.discord.listener.command

import com.schoolwork.mgmt.server.discord.listener.EventBase
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import reactor.core.publisher.Mono

abstract class Command: EventBase<ChatInputInteractionEvent>() {
    abstract fun getName(): String
    final override fun dmOnly() = false
    public abstract override fun execute(event: ChatInputInteractionEvent): Mono<Void>
}