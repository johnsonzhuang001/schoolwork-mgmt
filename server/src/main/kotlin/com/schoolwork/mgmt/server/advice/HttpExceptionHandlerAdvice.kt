package com.schoolwork.mgmt.server.advice

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import com.schoolwork.mgmt.server.error.*
import com.schoolwork.mgmt.server.dto.ErrorDto

@ControllerAdvice
class HttpExceptionHandlerAdvice {
    @ExceptionHandler(HttpException::class)
    fun handleException(e: HttpException): ResponseEntity<ErrorDto> {
        return ResponseEntity.status(e.getStatus()).body(ErrorDto(e.getStatus().value(), e.message ?: ""))
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ErrorDto> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDto(HttpStatus.FORBIDDEN.value(), e.message ?: "Unauthorized"))
    }

    @ExceptionHandler(ValidationException::class)
    fun handleNotFoundException(e: ValidationException): ResponseEntity<ErrorDto> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto(HttpStatus.BAD_REQUEST.value(), e.message ?: "Bad Request"))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorDto> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDto(HttpStatus.NOT_FOUND.value(), e.message ?: "Not Found"))
    }

    @ExceptionHandler(UserNotInSessionException::class)
    fun handleUserNotInSessionException(e: UserNotInSessionException): ResponseEntity<ErrorDto> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDto(HttpStatus.UNAUTHORIZED.value(), e.message ?: "User not found in session"))
    }

    @ExceptionHandler(Throwable::class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun handleThrowable(e: Throwable): ResponseEntity<ErrorDto> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message ?: "Internal Server Error"))
    }
}