package com.dogsteven.anhcop.services.authentication

import com.dogsteven.anhcop.configurations.security.token_services.TokenService
import com.dogsteven.anhcop.repositories.UserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthenticationServiceImpl(
    @Qualifier("JWTService") private val jwtService: TokenService,
    private val userRepository: UserRepository
): AuthenticationService {
    override fun execute(command: AuthenticationCommand.Authenticate): AuthenticationCommand.Authenticate.Response {
        val token = jwtService.generateAccessToken(command.principal)

        return AuthenticationCommand.Authenticate.Response(
            token = token
        )
    }

    override fun execute(command: AuthenticationCommand.EnableUser): AuthenticationCommand.EnableUser.Response {
        val user = userRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User with id \"${command.id}\" does not exist"
            )

        user.enabled = true

        userRepository.save(user)

        return AuthenticationCommand.EnableUser.Response
    }

    override fun execute(command: AuthenticationCommand.DisableUser): AuthenticationCommand.DisableUser.Response {
        val user = userRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User with id \"${command.id}\" does not exist"
            )

        user.enabled = false

        userRepository.save(user)

        return AuthenticationCommand.DisableUser.Response
    }
}