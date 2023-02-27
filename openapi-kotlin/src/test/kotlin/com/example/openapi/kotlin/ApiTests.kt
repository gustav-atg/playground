package com.example.openapi.kotlin

import com.example.openapi.kotlin.service.PetsService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTests(@Autowired val restTemplate: TestRestTemplate, @Autowired val petsService: PetsService) {
    @Value(value = "\${local.server.port}")
    private val port = 0
    private val baseUrl get() = "http://localhost:$port"
    private val testPet = "{\"id\":\"d66e6848-dee7-47de-b5ad-83a7b794c4a0\",\"name\":\"test\"}"
    private val jsonHeaders: HttpHeaders
        get() = run {
            var headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            return headers
        }

    @BeforeEach
    fun setup() {
        petsService.clear()
    }

    @Test
    fun `should list all pets`() {
        assertThat(restTemplate.getForObject("$baseUrl/public/v1/pets", String::class.java)).isEqualTo("[]")
        assertThat(restTemplate.getForObject("$baseUrl/private/v1/pets", String::class.java)).isEqualTo("[]")
    }

    @Test
    fun `should store new pet`() {
        assertThat(
            restTemplate.postForObject(
                "$baseUrl/private/v1/pets",
                HttpEntity(testPet, jsonHeaders),
                String::class.java,
            ),
        ).isEqualTo(testPet)
        assertThat(restTemplate.getForObject("$baseUrl/private/v1/pets", String::class.java)).isEqualTo("[$testPet]")
        assertThat(restTemplate.getForObject("$baseUrl/public/v1/pets", String::class.java)).isEqualTo("[$testPet]")
    }

    @Test
    fun `should store delete pet`() {
        assertThat(
            restTemplate.postForObject(
                "$baseUrl/private/v1/pets",
                HttpEntity(testPet, jsonHeaders),
                String::class.java,
            ),
        ).isEqualTo(testPet)
        assertThat(
            restTemplate.delete(
                "$baseUrl/private/v1/pets/d66e6848-dee7-47de-b5ad-83a7b794c4a0",
                String::class.java,
            ),
        )
        assertThat(restTemplate.getForObject("$baseUrl/public/v1/pets", String::class.java)).isEqualTo("[]")
    }
}
