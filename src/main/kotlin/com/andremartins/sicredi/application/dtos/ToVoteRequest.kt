package com.andremartins.sicredi.application.dtos

import java.util.UUID

data class ToVoteRequest(val sessionId: UUID, val associateId: UUID, val vote: Boolean)