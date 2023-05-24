package com.andremartins.sicredi.application.dtos

import java.util.UUID

data class AssociateResponse(val id: UUID, val name: String, val cpf: String)