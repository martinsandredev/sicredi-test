package com.andremartins.sicredi.infrastructure.http

import com.andremartins.sicredi.application.CreateScheduleUseCase
import com.andremartins.sicredi.application.dtos.CreateScheduleRequest
import com.andremartins.sicredi.application.dtos.ScheduleResponse
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
    private val createScheduleUseCase: CreateScheduleUseCase
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
}