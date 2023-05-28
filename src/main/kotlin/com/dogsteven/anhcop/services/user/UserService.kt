package com.dogsteven.anhcop.services.user

import org.springframework.transaction.annotation.Transactional

interface UserService {
    @Transactional
    fun execute(command: UserCommand.GetProfile): UserCommand.GetProfile.Response

    @Transactional
    fun execute(command: UserCommand.UpdateProfile): UserCommand.UpdateProfile.Response

    @Transactional
    fun execute(command: UserCommand.EnableUser): UserCommand.EnableUser.Response

    @Transactional
    fun execute(command: UserCommand.DisableUser): UserCommand.DisableUser.Response
}