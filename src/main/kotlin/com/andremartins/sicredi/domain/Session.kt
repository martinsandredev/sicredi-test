package com.andremartins.sicredi.domain

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
data class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val externalId: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    val schedule: Schedule,

    @Column(nullable = false)
    val time: SessionTime,

    @Column(nullable = false)
    val timeUnit: SessionTimeUnit,

    @Column(nullable = false)
    val closingDate: LocalDateTime,

    @Column(nullable = false)
    val closed: Boolean = false,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) : Serializable {
    fun isOpenToVote(): Boolean {
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.isBefore(closingDate)
    }

    companion object {
        fun dateToClose(initialDate: LocalDateTime, time: SessionTime, timeUnit: SessionTimeUnit): LocalDateTime =
            initialDate.plus(time.toLong(), timeUnit.toChronoUnit())
    }
}