package com.schoolwork.mgmt.server.discord.listener

import com.schoolwork.mgmt.server.dto.SignupRequest
import com.schoolwork.mgmt.server.service.UserService
import com.schoolwork.mgmt.server.utils.PasswordUtils
import discord4j.common.util.Snowflake
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.entity.channel.PrivateChannel
import discord4j.core.util.EntityUtil
import discord4j.discordjson.json.DMCreateRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ChatInputInteractionListener(
    private val discordClient: GatewayDiscordClient,
    private val userService: UserService,
): EventListener<ChatInputInteractionEvent>() {
    final override fun getEventType() = ChatInputInteractionEvent::class
    final override fun dmOnly() = false
    final override fun getClient() = discordClient

    override fun execute(event: ChatInputInteractionEvent): Mono<Void> {
        var message = "Please use the available commands to communicate with me."
        if (event.commandName == "startcoolcodehacking") {
            val author = event.interaction.user
            val discordUserId = author.id.asLong()
            val password = generatePassword()
            userService.getUserByDiscordId(discordUserId)?.let {
                message = "You have already started the challenge. Please use the registered account to continue."
            } ?: run {
                userService.signup(SignupRequest(
                    username = author.username,
                    password = password,
                    nickname = author.username,
                    discordUserId = discordUserId,
                ))
                message = "Student account has been set up. Please use this username and password to sign in: ${author.username} and $password"
            }

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
                        it.createMessage(message)
                    }.block()
                return event.reply()
                    .withEphemeral(true)
                    .withContent("Your command is well received! Please check my DM.")
            } else {
                return event.reply(message)
            }
        }
        return event.reply(message)
    }

    private fun generatePassword(): String {
        var password = PasswordUtils.generateRandomPassword(12)
        while (!PasswordUtils.isValidPassword(password)) {
            password = PasswordUtils.generateRandomPassword(12)
        }
        return password
    }
}