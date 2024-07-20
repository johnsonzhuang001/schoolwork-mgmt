package com.coolcode.server.discord.listener

import discord4j.core.event.domain.Event
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

abstract class EventListener<T: Event>: EventBase<T>() {
    abstract fun getEventType(): KClass<T>

    fun process(event: T): Mono<Void> {
        // Determine if the event can be processed
        if (event is MessageCreateEvent) {
            val author = event.message.author.get()
            if (author.isBot) return Mono.empty()
            return if (dmOnly()) {
                if (!isFromDm(event)) {
                    Mono.empty()
                } else {
                    execute(event)
                }
            } else {
                if (!isFromDm(event) && !isFromChannel(event)) {
                    Mono.empty()
                } else {
                    execute(event)
                }
            }
        } else if (event is ChatInputInteractionEvent) {
            if (dmOnly()) {
                return if (!isFromDm(event)) {
                    event.reply()
                        .withEphemeral(true)
                        .withContent("Please send this command to me in DM :)")
                } else {
                    execute(event)
                }
            } else {
                return if (!isFromDm(event) && !isFromChannel(event)) {
                    event.reply()
                        .withEphemeral(true)
                        .withContent("Wrong channel. Please use the command at #coolcodehacking.")
                } else {
                    execute(event)
                }
            }
        } else {
            return Mono.empty()
        }
    }
}