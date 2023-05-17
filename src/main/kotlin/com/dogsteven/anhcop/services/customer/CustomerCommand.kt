package com.dogsteven.anhcop.services.customer

import com.dogsteven.anhcop.entities.User

object CustomerCommand {
    class Register(
        val username: String,
        val password: String,
        val name: String,
        val phone: String
    ) {
        class Response(
            val profile: User.Model
        )
    }
}