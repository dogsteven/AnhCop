package com.dogsteven.anhcop.services.authentication

import org.springframework.transaction.annotation.Transactional

interface AuthenticationService {
    @Transactional
    fun execute(command: AuthenticationCommand.Authenticate): AuthenticationCommand.Authenticate.Response
}