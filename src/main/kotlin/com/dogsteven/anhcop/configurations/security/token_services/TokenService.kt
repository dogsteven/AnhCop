package com.dogsteven.anhcop.configurations.security.token_services

import com.dogsteven.anhcop.entities.User

interface TokenService {
    fun generateAccessToken(principal: User.Principal): String

    fun getUserIdFromToken(token: String): Long?
}