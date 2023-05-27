package com.andremartins.sicredi.infrastructure.adapters

import com.andremartins.sicredi.application.interfaces.VoteRepository
import com.andremartins.sicredi.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface JpaVoteRepository : JpaRepository<Vote, Long>, VoteRepository {
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN false ELSE true END FROM Vote v WHERE v.associate = :associate AND v.session = :session")
    override fun associateCanVote(@Param("associate") associate: Associate, @Param("session") session: Session): Boolean

    @Query(
        """SELECT new com.andremartins.sicredi.domain.SessionResult(
            COUNT(CASE WHEN v.vote = true THEN 1 END), 
            COUNT(CASE WHEN v.vote = false THEN 1 END)) 
                FROM Vote v 
                WHERE v.session = :session"""
    )
    override fun countSessionVotes(@Param("session") session: Session): SessionResult

    @Query("SELECT v FROM Vote v WHERE v.externalId = :externalId")
    fun findByExternalId(@Param("externalId") externalId: UUID): Vote?

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote")
    fun clear()
}