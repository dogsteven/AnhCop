package com.dogsteven.anhcop.utils

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [PositiveElements.Validator::class])
annotation class PositiveElements(
    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    object Validator: ConstraintValidator<PositiveElements, Collection<Int>> {
        override fun isValid(value: Collection<Int>?, context: ConstraintValidatorContext?): Boolean {
            return value == null || value.all { e -> e > 0 }
        }
    }
}