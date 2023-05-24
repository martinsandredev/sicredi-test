package com.andremartins.sicredi.application.interfaces

import com.andremartins.sicredi.domain.Associate
import java.util.UUID

interface AssociateRepository {
    fun findByExternalId(externalId: UUID): Associate?
    fun save(schedule: Associate)
}