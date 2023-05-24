package com.andremartins.sicredi.domain

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
data class Associate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val externalId: UUID,

    @Column(nullable = false, length = 50, columnDefinition = "VARCHAR (50)")
    val name: AssociateName,

    @Column(nullable = false, length = 11, columnDefinition = "VARCHAR (11)")
    val cpf: Cpf,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) : Serializable