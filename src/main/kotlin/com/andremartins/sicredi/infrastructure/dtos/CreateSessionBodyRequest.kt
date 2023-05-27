package com.andremartins.sicredi.infrastructure.dtos

import java.util.concurrent.TimeUnit

data class CreateSessionBodyRequest(val time: Long?, val timeUnit: TimeUnit?)