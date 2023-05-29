package com.andremartins.sicredi.infrastructure.http

import com.andremartins.sicredi.domain.DomainError
import com.andremartins.sicredi.domain.ValidationError
import com.andremartins.sicredi.infrastructure.dtos.ErrorResponse
import org.springframework.http.HttpStatus

fun mapValidationErrorToResponse(error: ValidationError): ErrorResponse {
    return when (error) {
        is ValidationError.InvalidScheduleName -> ErrorResponse(
            "invalid-schedule-name",
            "Schedule name must be between 3 and 120 characters"
        )

        is ValidationError.InvalidSessionTime -> ErrorResponse(
            "invalid-session-time",
            "Session time must be greater than or equal to 1"
        )

        is ValidationError.InvalidSessionTimeUnit -> ErrorResponse(
            "invalid-session-time-unit",
            "Session time unit must be MINUTES, HOURS or DAYS"
        )

        is ValidationError.InvalidAssociateName -> ErrorResponse(
            "invalid-associate-name",
            "Associate name must be between 3 and 50 characters"
        )

        is ValidationError.InvalidCpf -> ErrorResponse(
            "invalid-cpf",
            "Invalid CPF"
        )
    }
}

fun mapDomainErrorToResponse(error: DomainError): Pair<ErrorResponse, HttpStatus> {
    return when (error) {
        is DomainError.ScheduleNotFound -> Pair(
            ErrorResponse(
                "schedule-not-found",
                "Schedule not found"
            ), HttpStatus.NOT_FOUND
        )

        is DomainError.SessionNotFound -> Pair(
            ErrorResponse(
                "session-not-found",
                "Session not found"
            ), HttpStatus.NOT_FOUND
        )

        is DomainError.AssociateNotFound -> Pair(
            ErrorResponse(
                "associate-not-found",
                "Associate not found"
            ), HttpStatus.NOT_FOUND
        )

        is DomainError.AssociateAlreadyVoted -> Pair(
            ErrorResponse(
                "associate-already-voted",
                "Associate already voted"
            ), HttpStatus.NOT_ACCEPTABLE
        )

        is DomainError.ClosedSession -> Pair(
            ErrorResponse(
                "closed-session",
                "Closed voting session"
            ), HttpStatus.NOT_ACCEPTABLE
        )

        is DomainError.ResultOnlyAfterSessionClose -> Pair(
            ErrorResponse(
                "result-only-after-session-close",
                "Result only after session close"
            ), HttpStatus.NOT_ACCEPTABLE
        )
    }
}