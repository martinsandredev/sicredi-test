package com.andremartins.sicredi.infrastructure.dtos

import java.util.UUID

data class ToVoteBodyRequest(val associateId: UUID, val vote: Boolean)