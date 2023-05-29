package com.andremartins.sicredi.domain

sealed class DomainError : Throwable() {
    object ScheduleNotFound : DomainError()
    object SessionNotFound : DomainError()
    object AssociateNotFound : DomainError()
    object AssociateAlreadyVoted : DomainError()
    object ClosedSession : DomainError()
    object ResultOnlyAfterSessionClose : DomainError()
}