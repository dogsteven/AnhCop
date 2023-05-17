package com.dogsteven.anhcop.services.user

import com.dogsteven.anhcop.entities.User

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
                val password: String?,
                val name: String?
            ): Metadata

            class Employee(
                val password: String?,
                val name: String?,
                val phone: String?,
            ): Metadata

            class Customer(
                val password: String?,
                val name: String?,
                val phone: String?,
            ): Metadata
        }

        object Response
    }
}