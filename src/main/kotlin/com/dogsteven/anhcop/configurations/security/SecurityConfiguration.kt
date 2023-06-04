package com.dogsteven.anhcop.configurations.security

import com.dogsteven.anhcop.configurations.security.filters.BearerAuthenticationFilter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true
)
class SecurityConfiguration {
    @Bean
    fun userDetailsService(
        userPrincipalProvider: UserPrincipalProvider
    ): UserDetailsService = UserDetailsService { username ->
        userPrincipalProvider.getPrincipalByUsername(username)
            ?: throw UsernameNotFoundException(
                "User with username \"$username\" does not exist"
            )
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider = DaoAuthenticationProvider().apply {
        setUserDetailsService(userDetailsService)
        setPasswordEncoder(passwordEncoder)
    }

    @Bean
    @Order(1)
    fun usernameAndPasswordFilterChain(
        httpSecurity: HttpSecurity,
        authenticationProvider: AuthenticationProvider,
        authenticationEntryPoint: AuthenticationEntryPoint,
        accessDeniedEntryPoint: AccessDeniedEntryPoint,
        userDetailsService: UserDetailsService
    ): SecurityFilterChain {
        return httpSecurity
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedEntryPoint)
            .and()
            .securityMatcher(
                "/authentication/authenticate",
                "/api/customer/register",
                "/api/product/*/image",
                "/api/test/**"
            )
            .authorizeHttpRequests()
            .requestMatchers("/authentication/authenticate")
            .authenticated()
            .requestMatchers(
                "/api/customer/register",
                "/api/product/*/image",
                "/api/test/**"
            )
            .permitAll()
            .and()
            .httpBasic()
            .and()
            .authenticationProvider(authenticationProvider)
            .userDetailsService(userDetailsService)
            .build()
    }

    @Bean
    @Order(2)
    fun jwtAuthenticationFilterChain(
        httpSecurity: HttpSecurity,
        authenticationProvider: AuthenticationProvider,
        authenticationEntryPoint: AuthenticationEntryPoint,
        accessDeniedEntryPoint: AccessDeniedEntryPoint,
        userDetailsService: UserDetailsService,
        @Qualifier("JWTAuthenticationFilter") jwtAuthenticationFilter: BearerAuthenticationFilter
    ): SecurityFilterChain {
        return httpSecurity
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedEntryPoint)
            .and()
            .securityMatcher("/api/**", "/authentication/**")
            .authorizeHttpRequests()
            .anyRequest()
            .authenticated()
            .and()
            .authenticationProvider(authenticationProvider)
            .userDetailsService(userDetailsService)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}