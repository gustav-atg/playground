package com.example.openapi.kotlin.model

import com.example.openapi.generated.api.dto.PetDto
import java.util.UUID

class Pet(
    val id: UUID,
    val name: String,
)

fun Pet.mapToDto(): PetDto {
    return PetDto(id, name)
}

fun PetDto.mapToDomain(): Pet {
    return Pet(id, name)
}
