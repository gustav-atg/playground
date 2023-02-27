package com.example.openapi.kotlin.service

import com.example.openapi.kotlin.model.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PetsService(val storage: MutableSet<Pet> = mutableSetOf()) {
    fun one(id: UUID): Pet? {
        return storage.find { it.id == id }
    }

    fun all(): Flow<Pet> {
        return storage.iterator().asFlow()
    }

    fun update(item: Pet): Pet {
        storage.add(item)
        return item
    }

    fun delete(id: UUID): Boolean {
        return storage.removeIf { it.id == id }
    }

    fun clear() {
        storage.clear()
    }
}
