package com.andremartins.sicredi.application.dtos

import java.util.UUID
import java.util.concurrent.TimeUnit

data class SessionResponse(val id: UUID, val scheduleId: UUID, val time: Long, val timeUnit: TimeUnit)