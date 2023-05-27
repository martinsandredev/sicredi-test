package com.andremartins.sicredi.domain

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
data class Vote(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val externalId: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    val session: Session,

    @ManyToOne(fetch = FetchType.LAZY)
    val associate: Associate,

    @Column(nullable = false)
    val vote: Boolean,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) : Serializable