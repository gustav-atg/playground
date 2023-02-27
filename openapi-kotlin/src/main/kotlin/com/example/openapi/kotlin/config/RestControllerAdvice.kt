package com.example.openapi.kotlin.config

import com.example.openapi.generated.api.dto.ErrorDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebInputException

@RestControllerAdvice
class RestControllerAdvice {
    @ExceptionHandler(ServerWebInputException::class)
    fun serverWebInputExceptionHandler(ex: ServerWebInputException): ResponseEntity<ErrorDto> {
        return ResponseEntity(
            ErrorDto(
                code = HttpStatus.BAD_REQUEST.value(),
                message = ex.message,
            ),
            HttpStatus.BAD_REQUEST,
        )
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun responseStatusExceptionHandler(ex: ResponseStatusException): ResponseEntity<ErrorDto> {
        return ResponseEntity(
            ErrorDto(
                code = ex.statusCode.value(),
                message = ex.message,
            ),
            ex.statusCode,
        )
    }
}
