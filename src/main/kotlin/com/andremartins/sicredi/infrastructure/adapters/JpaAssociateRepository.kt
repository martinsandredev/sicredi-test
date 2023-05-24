package com.andremartins.sicredi.infrastructure.adapters

import com.andremartins.sicredi.application.interfaces.AssociateRepository
import com.andremartins.sicredi.domain.Associate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface JpaAssociateRepository : JpaRepository<Associate, Long>, AssociateRepository {
    @Query("SELECT a FROM Associate a WHERE a.externalId = :externalId")
    override fun findByExternalId(@Param("externalId") externalId: UUID): Associate?

    @Modifying
    @Transactional
    @Query("DELETE FROM Associate")
    fun clear()
}