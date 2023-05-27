package com.andremartins.sicredi.application.dtos

import java.util.UUID
import java.util.concurrent.TimeUnit

data class CreateSessionRequest(val scheduleId: UUID, val time: Long?, val timeUnit: TimeUnit?)