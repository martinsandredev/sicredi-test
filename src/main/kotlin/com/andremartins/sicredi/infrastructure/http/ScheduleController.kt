package com.andremartins.sicredi.infrastructure.http

import com.andremartins.sicredi.application.CreateScheduleUseCase
import com.andremartins.sicredi.application.CreateSessionUseCase
import com.andremartins.sicredi.application.dtos.CreateScheduleRequest
import com.andremartins.sicredi.application.dtos.CreateSessionRequest
import com.andremartins.sicredi.application.dtos.ScheduleResponse
import com.andremartins.sicredi.application.dtos.SessionResponse
import com.andremartins.sicredi.infrastructure.dtos.CreateSessionBodyRequest
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/schedules")
class ScheduleController(
    @Autowired
    private val createScheduleUseCase: CreateScheduleUseCase,
    @Autowired
    private val createSessionUseCase: CreateSessionUseCase
) {
    @PostMapping
    @Transactional
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Success", content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    value = """
                    {
                        "data": {
                            "id": "6c609218-076d-4ef1-9a1a-c8de2d0cf797",
                            "name": "Name",
                            "description": "Description"
                        }
                    }
                """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    value = """
                    {
                        "error": {
                            "code": "code",
                            "message": "Message"
                        }
                    }
                """
                )]
            )]
        )
    )
    fun createSchedule(@RequestBody body: CreateScheduleRequest): ResponseEntity<Response<ScheduleResponse>> {
        val res = createScheduleUseCase.execute(body)
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.Success(res))
    }

    @PostMapping("/{scheduleId}/sessions")
    @Transactional
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Success", content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    value = """
                    {
                        "data": {
                            "id": "6c609218-076d-4ef1-9a1a-c8de2d0cf797",
                            "scheduleId": "5a8b001b-39da-40c4-b7e2-2e7afdde54f6",
                            "time": 5,
                            "timeUnit": "MINUTES"
                        }
                    }
                """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    value = """
                    {
                        "error": {
                            "code": "code",
                            "message": "Message"
                        }
                    }
                """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    value = """
                    {
                        "error": {
                            "code": "code",
                            "message": "Message"
                        }
                    }
                """
                )]
            )]
        )
    )
    fun createSession(
        @PathVariable scheduleId: UUID,
        @RequestBody(required = false) body: CreateSessionBodyRequest?
    ): ResponseEntity<Response<SessionResponse>> {
        val res = createSessionUseCase.execute(CreateSessionRequest(scheduleId, body?.time, body?.timeUnit))
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.Success(res))
    }
}