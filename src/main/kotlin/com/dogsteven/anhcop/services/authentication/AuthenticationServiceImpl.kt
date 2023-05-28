package com.dogsteven.anhcop.services.authentication

import com.dogsteven.anhcop.configurations.security.token_services.TokenService
import com.dogsteven.anhcop.repositories.UserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(
    @Qualifier("JWTService") private val jwtService: TokenService
): AuthenticationService {
    override fun execute(command: AuthenticationCommand.Authenticate): AuthenticationCommand.Authenticate.Response {
        val token = jwtService.generateAccessToken(command.principal)

        return AuthenticationCommand.Authenticate.Response(
            token = token
        )
    }
}