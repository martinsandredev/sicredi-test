package com.andremartins.sicredi.infrastructure

import com.andremartins.sicredi.application.interfaces.UUIDGenerator
import java.util.*

class QueueBasedUUIDGenerator : UUIDGenerator {
    private val uuidQueue: Queue<UUID> = LinkedList()

    override fun generateUUID(): UUID {
        return uuidQueue.poll()
    }

    fun addUUIDToQueue(uuid: UUID) {
        uuidQueue.offer(uuid)
    }
}