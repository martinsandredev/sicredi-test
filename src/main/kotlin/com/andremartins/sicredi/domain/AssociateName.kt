package com.andremartins.sicredi.domain

@JvmInline
value class AssociateName(private val value: String) {
    init {
        if (value.length !in 3..50) {
            throw ValidationError.InvalidAssociateName(value)
        }
    }

    override fun toString() = value
}
