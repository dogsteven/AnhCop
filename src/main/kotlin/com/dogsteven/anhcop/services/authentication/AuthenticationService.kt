package com.dogsteven.anhcop.services.authentication

import org.springframework.transaction.annotation.Transactional

interface AuthenticationService {
    @Transactional
    fun execute(command: AuthenticationCommand.Authenticate): AuthenticationCommand.Authenticate.Response

    @Transactional
    fun execute(command: AuthenticationCommand.EnableUser): AuthenticationCommand.EnableUser.Response

    @Transactional
    fun execute(command: AuthenticationCommand.DisableUser): AuthenticationCommand.DisableUser.Response
}