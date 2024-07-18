package com.schoolwork.mgmt.server.discord.config

import com.schoolwork.mgmt.server.discord.listener.EventListener
import discord4j.common.JacksonResources
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.Event
import discord4j.discordjson.json.ApplicationCommandData
import discord4j.discordjson.json.ApplicationCommandRequest
import jakarta.annotation.PostConstruct
import org.apache.logging.log4j.LogManager
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component


@Component
class EventListenerAndCommandRegistrar<T: Event>(
    private val discordClient: GatewayDiscordClient,
    private val eventListeners: List<EventListener<T>>,
) {

    companion object {
        private val commandsFolderName: String = "discord/command"
        private val logger = LogManager.getLogger()
    }

    @PostConstruct
    fun registerEventListeners() {
        for (eventListener in eventListeners) {
            logger.info("Registering ${eventListener.javaClass.name}")
            discordClient.on(eventListener.getEventType().java) {
                eventListener.process(it)
            }.subscribe()
        }
    }

    @PostConstruct
    fun registerCommands() {
        //Create an ObjectMapper that supported Discord4J classes
        val d4jMapper = JacksonResources.create()

        // Convenience variables for the sake of easier to read code below.
        val matcher = PathMatchingResourcePatternResolver()
        val applicationService = discordClient.restClient.applicationService
        val applicationId = discordClient.restClient.applicationId.block() ?: throw IllegalStateException("No application id found")

        //Get our commands json from resources as command data
        val commands: MutableList<ApplicationCommandRequest> = ArrayList()
        for (resource in matcher.getResources("$commandsFolderName/*.json")) {
            val request = d4jMapper.objectMapper
                .readValue(resource.inputStream, ApplicationCommandRequest::class.java)

            commands.add(request)
        }

        /* Bulk overwrite commands. This is now idempotent, so it is safe to use this even when only 1 command
        is changed/added/removed
        */
        applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, commands)
            .doOnNext { ignore: ApplicationCommandData? -> logger.debug("Successfully registered Global Commands") }
            .doOnError { e ->
                logger.error("Failed to register global commands", e)
            }
            .subscribe()
    }
}