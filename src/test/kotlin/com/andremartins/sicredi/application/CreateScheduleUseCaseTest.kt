package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.CreateScheduleRequest
import com.andremartins.sicredi.domain.ValidationError
import com.andremartins.sicredi.infrastructure.QueueBasedUUIDGenerator
import com.andremartins.sicredi.infrastructure.adapters.JpaScheduleRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class CreateScheduleUseCaseTest(
    @Autowired
    private val scheduleRepository: JpaScheduleRepository,
) {
    @Test
    fun testCreateSchedule() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateScheduleUseCase(scheduleRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")

        uuidGenerator.addUUIDToQueue(id)

        val request = CreateScheduleRequest(name = "Test", description = "Test")
        val result = useCase.execute(request)
        val schedule = scheduleRepository.findByExternalId(id)

        assertNotNull(result)
        assertEquals(result.id, id)
        assertEquals(result.name, request.name)
        assertEquals(result.description, request.description)
        assertNotNull(schedule)
        assertEquals(schedule?.name.toString(), request.name)
        assertEquals(schedule?.description, request.description)
    }

    @Test
    fun testCreateSchedule_withInvalidScheduleName() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateScheduleUseCase(scheduleRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")

        uuidGenerator.addUUIDToQueue(id)

        val request = CreateScheduleRequest(name = "", description = "Test")
        assertThrows(ValidationError.InvalidScheduleName::class.java) { useCase.execute(request) }
    }

    @AfterEach
    fun cleanup() {
        scheduleRepository.clear()
    }
}