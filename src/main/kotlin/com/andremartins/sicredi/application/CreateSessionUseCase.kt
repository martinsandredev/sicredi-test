package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.CreateSessionRequest
import com.andremartins.sicredi.application.dtos.SessionResponse
import com.andremartins.sicredi.application.interfaces.ScheduleRepository
import com.andremartins.sicredi.application.interfaces.SessionRepository
import com.andremartins.sicredi.application.interfaces.UUIDGenerator
import com.andremartins.sicredi.domain.DomainError
import com.andremartins.sicredi.domain.Session
import com.andremartins.sicredi.domain.SessionTime
import com.andremartins.sicredi.domain.SessionTimeUnit
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class CreateSessionUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val sessionRepository: SessionRepository,
    private val uuidGenerator: UUIDGenerator
) {
    fun execute(request: CreateSessionRequest): SessionResponse {
        val schedule = scheduleRepository.findByExternalId(request.scheduleId) ?: throw DomainError.ScheduleNotFound
        val externalId = uuidGenerator.generateUUID()
        val time = SessionTime(request.time ?: 1)
        val timeUnit = SessionTimeUnit(request.time?.let { request.timeUnit } ?: TimeUnit.MINUTES)
        val session = Session(
            externalId = externalId,
            schedule = schedule,
            time = time,
            timeUnit = timeUnit,
            closingDate = Session.dateToClose(LocalDateTime.now(), time, timeUnit)
        )
        sessionRepository.save(session)
        return SessionResponse(
            session.externalId,
            session.schedule.externalId,
            session.time.toLong(),
            session.timeUnit.value
        )
    }
}