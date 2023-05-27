package com.andremartins.sicredi.domain

import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@JvmInline
value class SessionTimeUnit(val value: TimeUnit) {
    init {
        if (value !in listOf(TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS)) {
            throw ValidationError.InvalidSessionTimeUnit
        }
    }

    override fun toString() = value.toString()

    fun toChronoUnit() = when (value) {
        TimeUnit.MINUTES -> ChronoUnit.MINUTES
        TimeUnit.HOURS -> ChronoUnit.HOURS
        TimeUnit.DAYS -> ChronoUnit.DAYS
        else -> throw ValidationError.InvalidSessionTimeUnit
    }
}
