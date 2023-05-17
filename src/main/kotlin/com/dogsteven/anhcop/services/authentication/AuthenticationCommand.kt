package com.dogsteven.anhcop.services.authentication

import com.dogsteven.anhcop.entities.User

object AuthenticationCommand {
    class Authenticate(
        val principal: User.Principal
    ) {
        class Response(
            val token: String
        )
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