package com.dogsteven.anhcop.utils

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [NullOrNotBlank.Validator::class])
annotation class NullOrNotBlank(
    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    object Validator: ConstraintValidator<NullOrNotBlank, String> {
        override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
            return value == null || value.isNotBlank()
        }
    }
}