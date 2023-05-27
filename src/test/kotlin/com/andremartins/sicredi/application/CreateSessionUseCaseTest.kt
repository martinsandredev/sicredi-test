package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.CreateSessionRequest
import com.andremartins.sicredi.domain.DomainError
import com.andremartins.sicredi.domain.Schedule
import com.andremartins.sicredi.domain.ScheduleName
import com.andremartins.sicredi.domain.ValidationError
import com.andremartins.sicredi.infrastructure.QueueBasedUUIDGenerator
import com.andremartins.sicredi.infrastructure.adapters.JpaScheduleRepository
import com.andremartins.sicredi.infrastructure.adapters.JpaSessionRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest
@ActiveProfiles("test")
class CreateSessionUseCaseTest(
    @Autowired
    private val scheduleRepository: JpaScheduleRepository,
    @Autowired
    private val sessionRepository: JpaSessionRepository,
) {
    @Test
    fun testCreateSession() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateSessionUseCase(scheduleRepository, sessionRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val scheduleId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")

        uuidGenerator.addUUIDToQueue(id)
        scheduleRepository.save(Schedule(externalId = scheduleId, name = ScheduleName("Test"), description = "Test"))

        val request = CreateSessionRequest(scheduleId = scheduleId, time = 5, timeUnit = TimeUnit.MINUTES)
        val result = useCase.execute(request)
        val session = sessionRepository.findByExternalId(id)

        assertNotNull(result)
        assertEquals(result.id, id)
        assertEquals(result.time, request.time)
        assertEquals(result.timeUnit, request.timeUnit)
        assertNotNull(session)
        assertEquals(session?.time?.toLong(), request.time)
        assertEquals(session?.timeUnit?.value, request.timeUnit)
    }

    @Test
    fun testCreateSchedule_withScheduleNotFound() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateSessionUseCase(scheduleRepository, sessionRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val scheduleId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")

        uuidGenerator.addUUIDToQueue(id)

        val request = CreateSessionRequest(scheduleId = scheduleId, time = 5, timeUnit = TimeUnit.MINUTES)
        assertThrows(DomainError.ScheduleNotFound::class.java) { useCase.execute(request) }
    }

    @Test
    fun testCreateSchedule_withInvalidSessionTime() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateSessionUseCase(scheduleRepository, sessionRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val scheduleId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")

        uuidGenerator.addUUIDToQueue(id)
        scheduleRepository.save(Schedule(externalId = scheduleId, name = ScheduleName("Test"), description = "Test"))

        val request = CreateSessionRequest(scheduleId = scheduleId, time = 0, timeUnit = TimeUnit.MINUTES)
        assertThrows(ValidationError.InvalidSessionTime::class.java) { useCase.execute(request) }
    }

    @Test
    fun testCreateSchedule_withInvalidSessionTimeUnit() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateSessionUseCase(scheduleRepository, sessionRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")
        val scheduleId = UUID.fromString("e48ed784-5024-46d9-ae18-26675d82855d")

        uuidGenerator.addUUIDToQueue(id)
        scheduleRepository.save(Schedule(externalId = scheduleId, name = ScheduleName("Test"), description = "Test"))

        val request = CreateSessionRequest(scheduleId = scheduleId, time = 5, timeUnit = TimeUnit.SECONDS)
        assertThrows(ValidationError.InvalidSessionTimeUnit::class.java) { useCase.execute(request) }
    }

    @AfterEach
    fun cleanup() {
        sessionRepository.clear()
        scheduleRepository.clear()
    }
}