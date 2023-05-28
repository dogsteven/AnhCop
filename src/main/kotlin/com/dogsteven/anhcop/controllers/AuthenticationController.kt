package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.services.authentication.AuthenticationCommand
import com.dogsteven.anhcop.services.authentication.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/authentication")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {
    @GetMapping("/authenticate")
    @ResponseBody
    fun authenticate(authentication: Authentication): AuthenticationCommand.Authenticate.Response {
        val principal = (authentication.principal as? User.Principal)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unsupported principal type"
            )

        val command = AuthenticationCommand.Authenticate(principal)

        return authenticationService.execute(command)
    }
}