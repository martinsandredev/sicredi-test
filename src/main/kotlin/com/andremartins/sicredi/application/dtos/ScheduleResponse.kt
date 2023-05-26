package com.andremartins.sicredi.application.dtos

import java.util.UUID

data class ScheduleResponse(val id: UUID, val name: String, val description: String)