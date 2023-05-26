package com.andremartins.sicredi.domain

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
data class Schedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val externalId: UUID,

    @Column(nullable = false, length = 120, columnDefinition = "VARCHAR (120)")
    val name: ScheduleName,

    @Column(nullable = false, length = 500, columnDefinition = "TEXT")
    val description: String,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) : Serializable