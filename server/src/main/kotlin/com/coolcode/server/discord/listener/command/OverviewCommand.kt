package com.coolcode.server.discord.listener.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class OverviewCommand: Command() {
    final override fun getName() = "coolcodehacking_overview"
    final override fun execute(event: ChatInputInteractionEvent): Mono<Void> {
        val message = """
            You are a junior hacker trying to enter the professional hacker community.
            There is a headcount, but you need to finish a mission first.
            You are asked to act as a student at CoolCode Education, a website giving assignments to students who are interested in programming.
            There are a mentor and a peer assigned to you, and unfortunately, not every student is as smart as you are.
            Therefore, your task is to help your peer get really good performance at CoolCode Education.
            Start your challenge with the command /coolcodehacking_start
        """.trimIndent()
        return event.reply(message)
            .withEphemeral(true)
    }
}