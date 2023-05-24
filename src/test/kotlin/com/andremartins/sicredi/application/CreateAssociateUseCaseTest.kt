package com.andremartins.sicredi.application

import com.andremartins.sicredi.application.dtos.CreateAssociateRequest
import com.andremartins.sicredi.domain.ValidationError
import com.andremartins.sicredi.infrastructure.QueueBasedUUIDGenerator
import com.andremartins.sicredi.infrastructure.adapters.JpaAssociateRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class CreateAssociateUseCaseTest(
    @Autowired
    private val associateRepository: JpaAssociateRepository,
) {
    @Test
    fun testCreateAssociate() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateAssociateUseCase(associateRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")

        uuidGenerator.addUUIDToQueue(id)

        val request = CreateAssociateRequest(name = "Test", cpf = "84890278036")
        val result = useCase.execute(request)
        val associate = associateRepository.findByExternalId(id)

        assertNotNull(result)
        assertEquals(result.id, id)
        assertEquals(result.name, request.name)
        assertEquals(result.cpf, request.cpf)
        assertNotNull(associate)
        assertEquals(associate?.name.toString(), request.name)
        assertEquals(associate?.cpf.toString(), request.cpf)
    }

    @Test
    fun testCreateAssociate_withInvalidAssociateName() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateAssociateUseCase(associateRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")

        uuidGenerator.addUUIDToQueue(id)

        val request = CreateAssociateRequest(name = "", cpf = "84890278036")
        assertThrows(ValidationError.InvalidAssociateName::class.java) { useCase.execute(request) }
    }

    @Test
    fun testCreateAssociate_withInvalidCpf() {
        val uuidGenerator = QueueBasedUUIDGenerator()
        val useCase = CreateAssociateUseCase(associateRepository, uuidGenerator)
        val id = UUID.fromString("17dcb3ac-b28b-4f5a-a61b-4a3a6cd4aafb")

        uuidGenerator.addUUIDToQueue(id)

        val request = CreateAssociateRequest(name = "Test", cpf = "12345678912")
        assertThrows(ValidationError.InvalidCpf::class.java) { useCase.execute(request) }
    }

    @AfterEach
    fun cleanup() {
        associateRepository.clear()
    }
}