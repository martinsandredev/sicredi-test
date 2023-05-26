package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.CreateScheduleRequest
import com.andremartins.sicredi.application.dtos.ScheduleResponse
import com.andremartins.sicredi.application.interfaces.ScheduleRepository
import com.andremartins.sicredi.application.interfaces.UUIDGenerator
import com.andremartins.sicredi.domain.Schedule
import com.andremartins.sicredi.domain.ScheduleName
import org.springframework.stereotype.Service

@Service
class CreateScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val uuidGenerator: UUIDGenerator
) {
    fun execute(request: CreateScheduleRequest): ScheduleResponse {
        val externalId = uuidGenerator.generateUUID()
        val schedule = Schedule(
            externalId = externalId,
            name = ScheduleName(request.name),
            description = request.description
        )
        scheduleRepository.save(schedule)
        return ScheduleResponse(schedule.externalId, schedule.name.toString(), schedule.description)
    }
}