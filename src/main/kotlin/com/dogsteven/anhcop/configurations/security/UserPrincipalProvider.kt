package com.dogsteven.anhcop.configurations.security

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.repositories.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class UserPrincipalProvider(
    private val userRepository: UserRepository
) {
    fun getPrincipalById(id: Long): User.Principal? = userRepository.findByIdOrNull(id)?.principal

    fun getPrincipalByUsername(username: String): User.Principal? = userRepository.findByUsername(username)?.principal
}