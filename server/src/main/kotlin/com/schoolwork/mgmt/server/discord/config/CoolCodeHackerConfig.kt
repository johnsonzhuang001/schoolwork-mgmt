package com.schoolwork.mgmt.server.discord.config

import discord4j.core.DiscordClient
import discord4j.core.GatewayDiscordClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CoolCodeHackerConfig {
    @Value("\${discord.coolcodehacker.token}")
    lateinit var token: String

    @Bean
    fun discordClient(): GatewayDiscordClient? {
        return DiscordClient.create(token)
            .login()
            .block()
    }
}