package com.dogsteven.anhcop.services.employee

import com.dogsteven.anhcop.utils.VietnameseRegex
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

object EmployeeCommand {
    class CreateEmployee(
        @field:Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_.]*", message = "Username must contain only letters, numbers, \"_\" and \".\"")
        val username: String,
        @field:Length(min = 8, message = "Password must have at least 8 characters")
        val password: String,
        @field:Pattern(
            regexp = "([${VietnameseRegex.DIACRITIC_CHARACTERS}]|[A-Z])+( ([${VietnameseRegex.DIACRITIC_CHARACTERS}]|[A-Z])+)*",
            flags = [Pattern.Flag.CANON_EQ, Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE],
            message = "Wrong name pattern"
        )
        val name: String,
        @field:Pattern(regexp = "0[0-9]{9}", message = "Wrong phone numbers format")
        val phone: String
    ) {
        class Response(
            val id: Long
        )
    }

    class AssignEmployeeToVendor(
        val id: Long,
        val vendorId: Long?
    ) {
        object Response
    }
}