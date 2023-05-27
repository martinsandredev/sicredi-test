package com.andremartins.sicredi.domain

@JvmInline
value class SessionTime(private val value: Long) {
    init {
        if (value < 1) {
            throw ValidationError.InvalidSessionTime(value)
        }
    }

    fun toLong() = value
}
