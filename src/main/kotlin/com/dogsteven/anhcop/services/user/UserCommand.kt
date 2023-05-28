package com.dogsteven.anhcop.services.user

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.utils.VietnameseRegex
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

object UserCommand {
    class GetProfile(
        val principal: User.Principal
    ) {
        class Response(
            val profile: User.Model
        )
    }

    class UpdateProfile(
        val principal: User.Principal,
        val metadata: Metadata
    ) {
        sealed interface Metadata {
            class Administrator(
                @field:Length(min = 8, message = "Password must have at least 8 characters")
                val password: String?,
                @field:Pattern(
                    regexp = "([${VietnameseRegex.DIACRITIC_CHARACTERS}]|[A-Z])+( ([${VietnameseRegex.DIACRITIC_CHARACTERS}]|[A-Z])+)*",
                    flags = [Pattern.Flag.CANON_EQ, Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE],
                    message = "Wrong name pattern"
                )
                val name: String?
            ): Metadata

            class Employee(
                @field:Length(min = 8, message = "Password must have at least 8 characters")
                val password: String?,
                @field:Pattern(
                    regexp = "([${VietnameseRegex.DIACRITIC_CHARACTERS}]|[A-Z])+( ([${VietnameseRegex.DIACRITIC_CHARACTERS}]|[A-Z])+)*",
                    flags = [Pattern.Flag.CANON_EQ, Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE],
                    message = "Wrong name pattern"
                )
                val name: String?,
                @field:Pattern(regexp = "0[0-9]{9}", message = "Wrong phone numbers format")
                val phone: String?
            ): Metadata

            class Customer(
                @field:Length(min = 8, message = "Password must have at least 8 characters")
                val password: String?,
                @field:Pattern(
                    regexp = "([${VietnameseRegex.DIACRITIC_CHARACTERS}]|[A-Z])+( ([${VietnameseRegex.DIACRITIC_CHARACTERS}]|[A-Z])+)*",
                    flags = [Pattern.Flag.CANON_EQ, Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE],
                    message = "Wrong name pattern"
                )
                val name: String?,
                @field:Pattern(regexp = "0[0-9]{9}", message = "Wrong phone numbers format")
                val phone: String?
            ): Metadata
        }

        object Response
    }

    class EnableUser(
        val id: Long
    ) {
        object Response
    }

    class DisableUser(
        val id: Long
    ) {
        object Response
    }
}