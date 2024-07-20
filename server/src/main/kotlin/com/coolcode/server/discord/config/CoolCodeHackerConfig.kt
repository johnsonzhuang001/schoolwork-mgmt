package com.coolcode.server.discord.config

import discord4j.core.DiscordClient
import discord4j.core.GatewayDiscordClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CoolCodeHackerConfig(
    @Value("\${discord.coolcodehacker.token1}")
    private val token: String,
    @Value("\${discord.coolcodehacker.channel}")
    private val channel: String,
) {
    @Bean
    fun discordClient(): GatewayDiscordClient? {
        return DiscordClient.create(token)
            .login()
            .block()
    }
}