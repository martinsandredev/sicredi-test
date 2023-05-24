package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.AssociateResponse
import com.andremartins.sicredi.application.dtos.CreateAssociateRequest
import com.andremartins.sicredi.application.interfaces.AssociateRepository
import com.andremartins.sicredi.application.interfaces.UUIDGenerator
import com.andremartins.sicredi.domain.Associate
import com.andremartins.sicredi.domain.AssociateName
import com.andremartins.sicredi.domain.Cpf
import org.springframework.stereotype.Service

@Service
class CreateAssociateUseCase(private val associateRepository: AssociateRepository, private val uuidGenerator: UUIDGenerator) {
    fun execute(request: CreateAssociateRequest): AssociateResponse {
        val externalId = uuidGenerator.generateUUID();
        val associate = Associate(
            externalId = externalId,
            name = AssociateName(request.name),
            cpf = Cpf(request.cpf)
        )
        associateRepository.save(associate)
        return AssociateResponse(associate.externalId, associate.name.toString(), associate.cpf.toString())
    }
}