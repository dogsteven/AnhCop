package com.dogsteven.anhcop.configurations.security.filters

import com.dogsteven.anhcop.configurations.security.UserPrincipalProvider
import com.dogsteven.anhcop.configurations.security.token_services.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.lang.RuntimeException

abstract class BearerAuthenticationFilter(
    private val userPrincipalProvider: UserPrincipalProvider,
    private val tokenService: TokenService
): OncePerRequestFilter() {
    abstract fun shouldProcess(token: String): Boolean

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (SecurityContextHolder.getContext().authentication != null) {
            return filterChain.doFilter(request, response)
        }

        val authorizationHeader = request.getHeader("Authorization")
            ?: return filterChain.doFilter(request, response)

        if (!authorizationHeader.startsWith("Bearer ")) {
            return filterChain.doFilter(request, response)
        }

        val token = authorizationHeader.substring(startIndex = 7)

        if (!shouldProcess(token)) {
            return filterChain.doFilter(request, response)
        }

        val principal = tokenService.getUserIdFromToken(token)
            ?.let(userPrincipalProvider::getPrincipalById)
            ?: throw RuntimeException("Invalid token: $token")

        val authentication = UsernamePasswordAuthenticationToken(principal, null, principal.authorities)
        SecurityContextHolder.getContext().authentication = authentication

        return filterChain.doFilter(request, response)
    }
}