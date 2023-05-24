package com.andremartins.sicredi.infrastructure.adapters

import com.andremartins.sicredi.application.interfaces.UUIDGenerator
import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDGeneratorImpl : UUIDGenerator {
    override fun generateUUID(): UUID = UUID.randomUUID()
}