package com.andremartins.sicredi.infrastructure.http

import com.andremartins.sicredi.infrastructure.dtos.ErrorResponse

sealed class Response<T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Failure<T>(val error: ErrorResponse) : Response<T>()
}