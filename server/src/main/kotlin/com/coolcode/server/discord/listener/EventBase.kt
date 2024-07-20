package com.coolcode.server.discord.listener

import discord4j.core.event.domain.Event
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.entity.channel.Channel
import org.springframework.beans.factory.annotation.Value
import reactor.core.publisher.Mono

abstract class EventBase<T: Event> {
    @Value("\${discord.coolcodehacker.channel}")
    lateinit var coolCodeHackerChannelId: String

    protected abstract fun dmOnly(): Boolean
    protected abstract fun execute(event: T): Mono<Void>

    protected fun isFromChannel(event: Event): Boolean {
        if (event is ChatInputInteractionEvent) {
            return event.interaction.channelId.asLong() == coolCodeHackerChannelId.toLong()
        } else if (event is MessageCreateEvent) {
            return event.message.channelId.asLong() == coolCodeHackerChannelId.toLong()
        }
        return false
    }

    protected fun isFromDm(event: Event): Boolean {
        if (event is ChatInputInteractionEvent) {
            return event.interaction.channel.block()?.type == Channel.Type.DM
        } else if (event is MessageCreateEvent) {
            return event.message.channel.block()?.type == Channel.Type.DM
        }
        return false
    }
}