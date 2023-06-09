package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.services.user.UserCommand
import com.dogsteven.anhcop.services.user.UserService
import com.google.gson.Gson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    @ResponseBody
    fun getProfile(authentication: Authentication): UserCommand.GetProfile.Response {
        val principal = (authentication.principal as? User.Principal)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unsupported principal type"
            )

        val command = UserCommand.GetProfile(principal)

        return userService.execute(command)
    }

    @PutMapping
    @ResponseBody
    fun updateProfile(
        authentication: Authentication,
        httpEntity: HttpEntity<String>
    ): UserCommand.UpdateProfile.Response {
        val requestBody = httpEntity.body ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty body")

        val principal = (authentication.principal as? User.Principal)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unsupported principal type"
            )

        val metadata = try {
            val gson = Gson()
            when (principal.user) {
                is User.Administrator -> gson.fromJson(
                    requestBody,
                    UserCommand.UpdateProfile.Metadata.Administrator::class.java
                )

                is User.Employee -> gson.fromJson(
                    requestBody,
                    UserCommand.UpdateProfile.Metadata.Employee::class.java
                )

                is User.Customer -> gson.fromJson(
                    requestBody,
                    UserCommand.UpdateProfile.Metadata.Customer::class.java
                )

                else -> throw ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unsupported user type"
                )
            }
        } catch (_: Throwable) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Wrong body format"
            )
        }

        val command = UserCommand.UpdateProfile(
            principal = principal,
            metadata = metadata
        )

        return userService.execute(command)
    }

    @PutMapping("/enable/{id}")
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun enableUser(authentication: Authentication, @PathVariable("id") id: Long): UserCommand.EnableUser.Response {
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

        val command = UserCommand.EnableUser(id)

        return userService.execute(command)
    }

    @PutMapping("/disable/{id}")
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun disableUser(authentication: Authentication, @PathVariable("id") id: Long): UserCommand.DisableUser.Response {
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

        val command = UserCommand.DisableUser(id)

        return userService.execute(command)
    }
}