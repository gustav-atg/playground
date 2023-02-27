package com.example.openapi.kotlin.controller

import com.example.openapi.generated.api.PublicPetsApi
import com.example.openapi.generated.api.dto.PetDto
import com.example.openapi.kotlin.model.mapToDto
import com.example.openapi.kotlin.service.PetsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
class PublicPetsController(@Autowired val petsService: PetsService) : PublicPetsApi {
    override fun all(): ResponseEntity<Flow<PetDto>> {
        return ResponseEntity.ok(petsService.all().map { it.mapToDto() })
    }

    override suspend fun read(id: UUID): ResponseEntity<PetDto> {
        val item = petsService.one(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(item.mapToDto())
    }
}
