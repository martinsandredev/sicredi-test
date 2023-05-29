package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.SessionResultRequest
import com.andremartins.sicredi.domain.*
import com.andremartins.sicredi.infrastructure.adapters.JpaAssociateRepository
import com.andremartins.sicredi.infrastructure.adapters.JpaScheduleRepository
import com.andremartins.sicredi.infrastructure.adapters.JpaSessionRepository
import com.andremartins.sicredi.infrastructure.adapters.JpaVoteRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest
@ActiveProfiles("test")
class SessionResultUseCaseTest(
    @Autowired
    private val associateRepository: JpaAssociateRepository,
    @Autowired
    private val scheduleRepository: JpaScheduleRepository,
    @Autowired
    private val voteRepository: JpaVoteRepository,
    @Autowired
    private val sessionRepository: JpaSessionRepository,
) {
    @Test
    fun testSessionResult() {
        val useCase = SessionResultUseCase(sessionRepository, voteRepository)
        val scheduleId = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val sessionId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")
        val schedule = Schedule(externalId = scheduleId, name = ScheduleName("Test"), description = "Test")
        val time = SessionTime(5)
        val timeUnit = SessionTimeUnit(TimeUnit.MINUTES)
        val localDateTime = LocalDateTime.now().minusMinutes(10)
        val session = Session(
            externalId = sessionId,
            time = time,
            timeUnit = timeUnit,
            schedule = schedule,
            closingDate = Session.dateToClose(
                localDateTime, time, timeUnit
            )
        )
        val votes = listOf(
            Vote(
                externalId = UUID.randomUUID(),
                associate = Associate(
                    externalId = UUID.randomUUID(),
                    name = AssociateName("Test"),
                    cpf = Cpf("84890278036")
                ),
                session = session,
                vote = true
            ),
            Vote(
                externalId = UUID.randomUUID(),
                associate = Associate(
                    externalId = UUID.randomUUID(),
                    name = AssociateName("Test"),
                    cpf = Cpf("63563296022")
                ),
                session = session,
                vote = true
            ),
            Vote(
                externalId = UUID.randomUUID(),
                associate = Associate(
                    externalId = UUID.randomUUID(),
                    name = AssociateName("Test"),
                    cpf = Cpf("56728369090")
                ),
                session = session,
                vote = true
            ),
            Vote(
                externalId = UUID.randomUUID(),
                associate = Associate(
                    externalId = UUID.randomUUID(),
                    name = AssociateName("Test"),
                    cpf = Cpf("43632932093")
                ),
                session = session,
                vote = false
            ),
            Vote(
                externalId = UUID.randomUUID(),
                associate = Associate(
                    externalId = UUID.randomUUID(),
                    name = AssociateName("Test"),
                    cpf = Cpf("55271480020")
                ),
                session = session,
                vote = false
            )
        )
        associateRepository.saveAll(votes.map { v -> v.associate })
        scheduleRepository.save(schedule)
        sessionRepository.save(session)
        voteRepository.saveAll(votes)

        val request = SessionResultRequest(sessionId = sessionId)
        val result = useCase.execute(request)

        assertNotNull(result)
        assertEquals(result.sessionId, sessionId)
        assertEquals(result.scheduleId, scheduleId)
        assertEquals(result.scheduleName, schedule.name.toString())
        assertEquals(result.yes, 3)
        assertEquals(result.no, 2)
    }

    @Test
    fun testCreateSchedule_withSessionNotFound() {
        val useCase = SessionResultUseCase(sessionRepository, voteRepository)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val request = SessionResultRequest(sessionId = id)
        assertThrows(DomainError.SessionNotFound::class.java) { useCase.execute(request) }
    }

    @Test
    fun testCreateSchedule_withResultOnlyAfterSessionClose() {
        val useCase = SessionResultUseCase(sessionRepository, voteRepository)
        val scheduleId = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val sessionId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")
        val schedule = Schedule(externalId = scheduleId, name = ScheduleName("Test"), description = "Test")
        val time = SessionTime(5)
        val timeUnit = SessionTimeUnit(TimeUnit.MINUTES)
        val localDateTime = LocalDateTime.now()
        val session = Session(
            externalId = sessionId,
            time = time,
            timeUnit = timeUnit,
            schedule = schedule,
            closingDate = Session.dateToClose(
                localDateTime, time, timeUnit
            )
        )

        scheduleRepository.save(schedule)
        sessionRepository.save(session)

        val request = SessionResultRequest(sessionId = sessionId)
        assertThrows(DomainError.ResultOnlyAfterSessionClose::class.java) { useCase.execute(request) }
    }

    @AfterEach
    fun cleanup() {
        voteRepository.clear()
        sessionRepository.clear()
        scheduleRepository.clear()
        associateRepository.clear()
    }
}