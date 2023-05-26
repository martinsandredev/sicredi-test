package com.andremartins.sicredi.application.interfaces

import com.andremartins.sicredi.domain.Schedule
import java.util.UUID

interface ScheduleRepository {
    fun findByExternalId(externalId: UUID): Schedule?
    fun save(schedule: Schedule)
}