package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.services.authentication.AuthenticationCommand
import com.dogsteven.anhcop.services.authentication.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
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

    @PutMapping("/enable/{id}")
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun enableUser(authentication: Authentication, @PathVariable("id") id: Long): AuthenticationCommand.EnableUser.Response {
        val principal = (authentication.principal as? User.Principal)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unsupported principal type"
            )

        if (principal.user.id == id) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Self-enabling is not permitted"
            )
        }

        val command = AuthenticationCommand.EnableUser(id)

        return authenticationService.execute(command)
    }

    @PutMapping("/disable/{id}")
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun disableUser(authentication: Authentication, @PathVariable("id") id: Long): AuthenticationCommand.DisableUser.Response {
        val principal = (authentication.principal as? User.Principal)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unsupported principal type"
            )

        if (principal.user.id == id) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Self-disabling is not permitted"
            )
        }

        val command = AuthenticationCommand.DisableUser(id)

        return authenticationService.execute(command)
    }
}