package com.andremartins.sicredi.application.dtos

import java.util.UUID

data class ToVoteResponse(val id: UUID, val sessionId: UUID, val associateId: UUID, val vote: Boolean)