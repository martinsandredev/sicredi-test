package com.andremartins.sicredi.domain

sealed class DomainError : Throwable() {
    object ScheduleNotFound : DomainError()
}