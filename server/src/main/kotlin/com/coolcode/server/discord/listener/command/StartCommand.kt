package com.coolcode.server.discord.listener.command

import com.coolcode.server.dto.SignupRequest
import com.coolcode.server.service.UserService
import com.coolcode.server.utils.PasswordUtils
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class StartCommand(
    private val userService: UserService,
): Command() {
    final override fun getName() = "coolcodehacking_start"
    final override fun execute(event: ChatInputInteractionEvent): Mono<Void> {
        var message = ""
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
            message = """
                Your student account has been set up. Please note down your username and password because both the challenge developer and I cannot help you retrieve your password.
                Username: ${author.username}
                Password: $password
            """.trimIndent()
        }
        return event.reply(message)
            .withEphemeral(true)
    }

    private fun generatePassword(): String {
        var password = PasswordUtils.generateRandomPassword(12)
        while (!PasswordUtils.isValidPassword(password)) {
            password = PasswordUtils.generateRandomPassword(12)
        }
        return password
    }
}