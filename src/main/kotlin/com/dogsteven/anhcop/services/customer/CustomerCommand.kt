package com.dogsteven.anhcop.services.customer

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.utils.VietnameseRegex
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

object CustomerCommand {
    class Register(
        @field:Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_.]*", message = "Username must contain only letters, numbers, \"_\" and \".\"")
        val username: String,
        @field:NotBlank(message = "Password must not be blank")
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
            val profile: User.Model
        )
    }
}