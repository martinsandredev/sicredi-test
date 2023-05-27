package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.ToVoteRequest
import com.andremartins.sicredi.application.dtos.ToVoteResponse
import com.andremartins.sicredi.application.interfaces.AssociateRepository
import com.andremartins.sicredi.application.interfaces.SessionRepository
import com.andremartins.sicredi.application.interfaces.UUIDGenerator
import com.andremartins.sicredi.application.interfaces.VoteRepository
import com.andremartins.sicredi.domain.DomainError
import com.andremartins.sicredi.domain.Vote
import org.springframework.stereotype.Service

@Service
class ToVoteUseCase(
    private val sessionRepository: SessionRepository,
    private val associateRepository: AssociateRepository,
    private val voteRepository: VoteRepository,
    private val uuidGenerator: UUIDGenerator
) {
    fun execute(request: ToVoteRequest): ToVoteResponse {
        val associate = associateRepository.findByExternalId(request.associateId) ?: throw DomainError.AssociateNotFound
        val session = sessionRepository.findByExternalId(request.sessionId) ?: throw DomainError.SessionNotFound
        if (!session.isOpenToVote()) {
            throw DomainError.ClosedSession
        }
        if (!voteRepository.associateCanVote(associate, session)) {
            throw DomainError.AssociateAlreadyVoted
        }
        val externalId = uuidGenerator.generateUUID()
        val vote = Vote(
            externalId = externalId,
            session = session,
            vote = request.vote,
            associate = associate
        )
        voteRepository.save(vote)
        return ToVoteResponse(
            vote.externalId,
            vote.session.externalId,
            vote.associate.externalId,
            vote.vote
        )
    }
}