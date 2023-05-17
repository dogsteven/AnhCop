package com.dogsteven.anhcop.configurations.security.filters

import com.dogsteven.anhcop.configurations.security.UserPrincipalProvider
import com.dogsteven.anhcop.configurations.security.token_services.TokenService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("JWTAuthenticationFilter")
class JWTAuthenticationFilter(
    userPrincipalProvider: UserPrincipalProvider,
    @Qualifier("JWTService") jwtService: TokenService
): BearerAuthenticationFilter(userPrincipalProvider, jwtService)