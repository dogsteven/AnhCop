package com.dogsteven.anhcop.configurations.exception_handling

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlingConfiguration {
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: ConstraintViolationException
    ) {
        response.sendError(
            HttpServletResponse.SC_BAD_REQUEST,
            "Invalid data: ${exception.message}"
        )
    }
}