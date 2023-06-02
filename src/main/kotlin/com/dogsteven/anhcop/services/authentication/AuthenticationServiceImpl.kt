package com.dogsteven.anhcop.services.authentication

import com.dogsteven.anhcop.configurations.security.token_services.TokenService
import com.dogsteven.anhcop.utils.GsonTypeAdapters
import com.google.gson.GsonBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class AuthenticationServiceImpl(
    @Qualifier("JWTService") private val jwtService: TokenService
): AuthenticationService {
    private val gson = GsonBuilder()
        .registerTypeAdapter(OffsetDateTime::class.java, GsonTypeAdapters.OffsetDateTimeGsonTypeAdapter)
        .create()

    override fun execute(command: AuthenticationCommand.Authenticate): AuthenticationCommand.Authenticate.Response {
        val token = jwtService.generateAccessToken(command.principal)
        return AuthenticationCommand.Authenticate.Response(token)
    }
}