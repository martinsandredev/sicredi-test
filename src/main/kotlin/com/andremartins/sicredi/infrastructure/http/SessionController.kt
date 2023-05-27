package com.andremartins.sicredi.infrastructure.http

import com.andremartins.sicredi.application.ToVoteUseCase
import com.andremartins.sicredi.application.dtos.*
import com.andremartins.sicredi.infrastructure.dtos.ToVoteBodyRequest
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
@RequestMapping("/api/v1/sessions")
class SessionController(
    @Autowired
    private val toVoteUseCase: ToVoteUseCase
) {
    @PostMapping("/{sessionId}/votes")
    @Transactional
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Success", content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    value = """
                    {
                        "data": {
                            "id": "e527aff0-069c-4aeb-ac41-c31e850deeff",
                            "sessionId": "6c609218-076d-4ef1-9a1a-c8de2d0cf797",
                            "associateId": "5a8b001b-39da-40c4-b7e2-2e7afdde54f6",
                            "vote": true
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
        ),
        ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
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
    )
    fun toVote(
        @PathVariable sessionId: UUID,
        @RequestBody body: ToVoteBodyRequest
    ): ResponseEntity<Response<ToVoteResponse>> {
        val res = toVoteUseCase.execute(ToVoteRequest(sessionId, body.associateId, body.vote))
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.Success(res))
    }
}