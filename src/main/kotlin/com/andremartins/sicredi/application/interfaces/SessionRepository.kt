package com.andremartins.sicredi.application.interfaces

import com.andremartins.sicredi.domain.Session
import java.time.LocalDateTime
import java.util.*

interface SessionRepository {
    fun findByExternalId(externalId: UUID): Session?
    fun findSessionsToClose(endDate: LocalDateTime): List<Session>
    fun save(session: Session)
}