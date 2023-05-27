package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.ToVoteRequest
import com.andremartins.sicredi.domain.*
import com.andremartins.sicredi.infrastructure.QueueBasedUUIDGenerator
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
class ToVoteUseCaseTest(
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
    fun testToVote() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = ToVoteUseCase(sessionRepository, associateRepository, voteRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val scheduleId = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val sessionId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")
        val associateId = UUID.fromString("7a094ce7-b6d4-4b7e-8130-3bb66771fb27")
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
        val associate = Associate(
            externalId = associateId,
            name = AssociateName("Test"),
            cpf = Cpf("84890278036")
        )
        associateRepository.save(associate)
        scheduleRepository.save(schedule)
        sessionRepository.save(session)
        uuidGenerator.addUUIDToQueue(id)

        val request = ToVoteRequest(sessionId = sessionId, associateId = associateId, vote = true)
        val result = useCase.execute(request)
        val vote = voteRepository.findByExternalId(id)

        assertNotNull(result)
        assertEquals(result.id, id)
        assertEquals(result.sessionId, sessionId)
        assertEquals(result.associateId, associateId)
        assertEquals(result.vote, true)
        assertNotNull(vote)
        assertEquals(vote?.vote, true)
    }

    @Test
    fun testCreateSchedule_withAssociateNotFound() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = ToVoteUseCase(sessionRepository, associateRepository, voteRepository, uuidGenerator)
        val sessionId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")
        val associateId = UUID.fromString("7a094ce7-b6d4-4b7e-8130-3bb66771fb27")
        val request = ToVoteRequest(sessionId = sessionId, associateId = associateId, vote = true)
        assertThrows(DomainError.AssociateNotFound::class.java) { useCase.execute(request) }
    }

    @Test
    fun testCreateSchedule_withSessionNotFound() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = ToVoteUseCase(sessionRepository, associateRepository, voteRepository, uuidGenerator)
        val sessionId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")
        val associateId = UUID.fromString("7a094ce7-b6d4-4b7e-8130-3bb66771fb27")
        val associate = Associate(
            externalId = associateId,
            name = AssociateName("Test"),
            cpf = Cpf("84890278036")
        )
        associateRepository.save(associate)
        val request = ToVoteRequest(sessionId = sessionId, associateId = associateId, vote = true)
        assertThrows(DomainError.SessionNotFound::class.java) { useCase.execute(request) }
    }

    @Test
    fun testCreateSchedule_withClosedSession() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = ToVoteUseCase(sessionRepository, associateRepository, voteRepository, uuidGenerator)
        val scheduleId = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val sessionId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")
        val associateId = UUID.fromString("7a094ce7-b6d4-4b7e-8130-3bb66771fb27")
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
        val associate = Associate(
            externalId = associateId,
            name = AssociateName("Test"),
            cpf = Cpf("84890278036")
        )
        associateRepository.save(associate)
        scheduleRepository.save(schedule)
        sessionRepository.save(session)
        val request = ToVoteRequest(sessionId = sessionId, associateId = associateId, vote = true)
        assertThrows(DomainError.ClosedSession::class.java) { useCase.execute(request) }
    }

    @Test
    fun testCreateSchedule_withAssociateAlreadyVoted() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = ToVoteUseCase(sessionRepository, associateRepository, voteRepository, uuidGenerator)
        val scheduleId = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val sessionId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")
        val associateId = UUID.fromString("7a094ce7-b6d4-4b7e-8130-3bb66771fb27")
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
        val associate = Associate(
            externalId = associateId,
            name = AssociateName("Test"),
            cpf = Cpf("84890278036")
        )
        val vote = Vote(
            externalId = UUID.randomUUID(),
            associate = associate,
            session = session,
            vote = true
        )
        associateRepository.save(associate)
        scheduleRepository.save(schedule)
        sessionRepository.save(session)
        voteRepository.save(vote)
        val request = ToVoteRequest(sessionId = sessionId, associateId = associateId, vote = true)
        assertThrows(DomainError.AssociateAlreadyVoted::class.java) { useCase.execute(request) }
    }

    @AfterEach
    fun cleanup() {
        voteRepository.clear()
        sessionRepository.clear()
        scheduleRepository.clear()
        associateRepository.clear()
    }
}