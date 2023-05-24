package com.andremartins.sicredi.infrastructure.http

import com.andremartins.sicredi.application.CreateAssociateUseCase
import com.andremartins.sicredi.application.dtos.*
import com.andremartins.sicredi.infrastructure.dtos.CreateAssociateBodyRequest
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
@RequestMapping("/api/v1/associates")
class AssociateController(
    @Autowired
    private val createAssociateUseCase: CreateAssociateUseCase,
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
                            "cpf": "12345678912"
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
    fun createAssociate(
        @RequestBody body: CreateAssociateBodyRequest
    ): ResponseEntity<Response<AssociateResponse>> {
        val res = createAssociateUseCase.execute(CreateAssociateRequest(body.name, body.cpf))
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.Success(res))
    }
}