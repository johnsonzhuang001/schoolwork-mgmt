package com.schoolwork.mgmt.server.discord.listener

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.entity.channel.Channel
import org.springframework.beans.factory.annotation.Value
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

abstract class EventListener<T: Event> {
    @Value("\${discord.coolcodehacker.channel}")
    lateinit var coolCodeHackerChannelId: String

    abstract fun getEventType(): KClass<T>
    protected abstract fun dmOnly(): Boolean
    protected abstract fun getClient(): GatewayDiscordClient
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