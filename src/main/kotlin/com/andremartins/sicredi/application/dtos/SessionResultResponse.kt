package com.andremartins.sicredi.application.dtos

import java.util.UUID

data class SessionResultResponse(
    val sessionId: UUID,
    val scheduleId: UUID,
    val scheduleName: String,
    val yes: Long,
    val no: Long
)