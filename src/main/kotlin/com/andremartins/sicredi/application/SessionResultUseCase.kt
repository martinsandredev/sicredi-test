package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.SessionResultRequest
import com.andremartins.sicredi.application.dtos.SessionResultResponse
import com.andremartins.sicredi.application.interfaces.SessionRepository
import com.andremartins.sicredi.application.interfaces.VoteRepository
import com.andremartins.sicredi.domain.*
import org.springframework.stereotype.Service

@Service
class SessionResultUseCase(
    private val sessionRepository: SessionRepository,
    private val voteRepository: VoteRepository,
) {
    fun execute(request: SessionResultRequest): SessionResultResponse {
        val session = sessionRepository.findByExternalId(request.sessionId) ?: throw DomainError.SessionNotFound
        if (session.isOpenToVote()) {
            throw DomainError.ResultOnlyAfterSessionClose
        }
        val result = voteRepository.countSessionVotes(session)
        return SessionResultResponse(
            session.externalId,
            session.schedule.externalId,
            session.schedule.name.toString(),
            result.yes,
            result.no
        )
    }
}