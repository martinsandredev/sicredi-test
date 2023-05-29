package com.andremartins.sicredi.infrastructure

import com.andremartins.sicredi.domain.DomainError
import com.andremartins.sicredi.domain.ValidationError
import com.andremartins.sicredi.infrastructure.http.Response
import com.andremartins.sicredi.infrastructure.http.mapDomainErrorToResponse
import com.andremartins.sicredi.infrastructure.http.mapValidationErrorToResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ValidationError::class)
    fun handleValidationError(exception: ValidationError): ResponseEntity<Response<Any>> {
        return ResponseEntity.badRequest()
            .body(Response.Failure(mapValidationErrorToResponse(exception)))
    }

    @ExceptionHandler(DomainError::class)
    fun handleDomainError(exception: DomainError): ResponseEntity<Response<Any>> {
        val (body, status) = mapDomainErrorToResponse(exception)
        return ResponseEntity.status(status)
            .body(Response.Failure(body))
    }
}
