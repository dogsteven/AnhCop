package com.dogsteven.anhcop.utils

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator

interface ValidatorExtension {
    companion object {
        fun<T> Validator.throwValidate(value: T) {
            val violations = validate(value)

            if (violations.isNotEmpty()) {
                throw ConstraintViolationException(violations)
            }
        }
    }
}