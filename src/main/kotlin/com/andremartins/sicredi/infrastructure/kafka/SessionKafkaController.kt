package com.andremartins.sicredi.infrastructure.kafka

import com.andremartins.sicredi.domain.SessionResult
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SessionKafkaController() {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @KafkaListener(topics = [KafkaHelpers.SESSION_CLOSED_TOPIC], groupId = KafkaHelpers.GROUP)
    fun handleSessionClosed(record: ConsumerRecord<String, String>) {
        logger.info("Session closed message received: ${record.value()}")
        val sessionResult = KafkaHelpers.fromJson<SessionResult>(record.value())
        logger.info(sessionResult.toString())
    }
}