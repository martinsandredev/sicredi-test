package com.andremartins.sicredi.infrastructure.task

import com.andremartins.sicredi.application.interfaces.Messenger
import com.andremartins.sicredi.application.interfaces.SessionRepository
import com.andremartins.sicredi.application.interfaces.VoteRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@EnableScheduling
class SessionTaskController(
    private val sessionRepository: SessionRepository,
    private val voteRepository: VoteRepository,
    private val messenger: Messenger
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(cron = "0 */1 * ? * *")
    @Transactional
    fun handleClosedSessions() {
        logger.info("Executing closed sessions task...")
        val closedSessions = sessionRepository.findSessionsToClose(LocalDateTime.now())

        closedSessions.forEach { session ->
            val updatedSession = session.copy(closed = true)
            sessionRepository.save(updatedSession)
            val result = voteRepository.countSessionVotes(session)
            messenger.send(Messenger.MessageType.SessionClosed, result)
        }

        logger.info("Closed sessions task executed!")
    }
}