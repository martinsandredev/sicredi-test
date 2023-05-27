package com.andremartins.sicredi.application.interfaces

import com.andremartins.sicredi.domain.Associate
import com.andremartins.sicredi.domain.Session
import com.andremartins.sicredi.domain.SessionResult
import com.andremartins.sicredi.domain.Vote

interface VoteRepository {
    fun associateCanVote(associate: Associate, session: Session): Boolean
    fun countSessionVotes(session: Session): SessionResult
    fun save(vote: Vote)
}