package com.schoolwork.mgmt.server.discord.listener.command

import com.schoolwork.mgmt.server.service.AssignmentService
import discord4j.common.util.Snowflake
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.entity.channel.PrivateChannel
import discord4j.core.util.EntityUtil
import discord4j.discordjson.json.DMCreateRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProgressCommand(
    private val discordClient: GatewayDiscordClient,
    private val assignmentService: AssignmentService,
): Command() {
    final override fun getName() = "coolcodehacking_progress"
    final override fun execute(event: ChatInputInteractionEvent): Mono<Void> {
        val author = event.interaction.user
        val discordUserId = author.id.asLong()
        if (isFromChannel(event)) {
            discordClient.restClient.userService
                .createDM(
                    DMCreateRequest
                        .builder()
                        .recipientId(Snowflake.asString(discordUserId))
                        .build()
                ).map {
                    EntityUtil.getChannel(discordClient, it)
                }.cast(PrivateChannel::class.java).flatMap {
                    it.createMessage(assignmentService.getProgress(discordUserId))
                }.block()
            return event.reply()
                .withEphemeral(true)
                .withContent("Your command is well received! Please check my DM.")
        } else {
            return event.reply(assignmentService.getProgress(discordUserId))
        }
    }
}