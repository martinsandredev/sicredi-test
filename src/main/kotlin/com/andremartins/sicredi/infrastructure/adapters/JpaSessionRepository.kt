package com.andremartins.sicredi.infrastructure.adapters

import com.andremartins.sicredi.application.interfaces.SessionRepository
import com.andremartins.sicredi.domain.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
interface JpaSessionRepository : JpaRepository<Session, Long>, SessionRepository {
    @Query("SELECT ses FROM Session ses WHERE ses.externalId = :externalId")
    override fun findByExternalId(@Param("externalId") externalId: UUID): Session?

    @Query("SELECT ses FROM Session ses WHERE ses.closingDate <= :endDate AND ses.closed = false")
    override fun findSessionsToClose(@Param("endDate") endDate: LocalDateTime): List<Session>

    @Modifying
    @Transactional
    @Query("DELETE FROM Session")
    fun clear()
}