package com.andremartins.sicredi.infrastructure.kafka

import com.andremartins.sicredi.application.interfaces.Messenger
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object KafkaHelpers {
    const val GROUP = "sicredi"
    const val SESSION_CREATED_TOPIC = "session-created"
    const val SESSION_CLOSED_TOPIC = "session-closed"

    fun getTopic(type: Messenger.MessageType) = when (type) {
        is Messenger.MessageType.SessionCreated -> SESSION_CREATED_TOPIC
        is Messenger.MessageType.SessionClosed -> SESSION_CLOSED_TOPIC
    }

    fun <T> toJson(data: T): String = jacksonObjectMapper().writeValueAsString(data)

    inline fun <reified T> fromJson(s: String): T = jacksonObjectMapper().readValue(s, object : TypeReference<T>() {})
}