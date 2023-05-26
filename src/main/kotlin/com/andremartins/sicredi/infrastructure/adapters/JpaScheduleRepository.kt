package com.andremartins.sicredi.infrastructure.adapters

import com.andremartins.sicredi.application.interfaces.ScheduleRepository
import com.andremartins.sicredi.domain.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface JpaScheduleRepository : JpaRepository<Schedule, Long>, ScheduleRepository {
    @Query("SELECT sch FROM Schedule sch WHERE sch.externalId = :externalId")
    override fun findByExternalId(@Param("externalId") externalId: UUID): Schedule?

    @Modifying
    @Transactional
    @Query("DELETE FROM Schedule")
    fun clear()
}