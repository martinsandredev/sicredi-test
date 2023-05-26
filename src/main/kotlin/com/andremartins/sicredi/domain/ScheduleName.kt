package com.andremartins.sicredi.domain

@JvmInline
value class ScheduleName(private val value: String) {
    init {
        if (value.length !in 3..120) {
            throw ValidationError.InvalidScheduleName(value)
        }
    }

    override fun toString() = value
}
