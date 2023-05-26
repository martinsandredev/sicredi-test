package com.andremartins.sicredi.domain

sealed class ValidationError : Throwable() {
    data class InvalidAssociateName(val name: String) : ValidationError()
    data class InvalidCpf(val cpf: String) : ValidationError()
    data class InvalidScheduleName(val name: String) : ValidationError()
}