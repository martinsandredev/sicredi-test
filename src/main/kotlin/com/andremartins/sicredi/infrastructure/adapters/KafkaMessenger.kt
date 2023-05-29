package com.andremartins.sicredi.infrastructure.adapters

import com.andremartins.sicredi.application.interfaces.Messenger
import com.andremartins.sicredi.infrastructure.kafka.KafkaHelpers
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaMessenger(
    private val kafkaTemplate: KafkaTemplate<String, String>
) : Messenger {
    override fun <T> send(type: Messenger.MessageType, payload: T) {
        val topic = KafkaHelpers.getTopic(type)
        kafkaTemplate.send(topic, KafkaHelpers.toJson(payload))
    }
}