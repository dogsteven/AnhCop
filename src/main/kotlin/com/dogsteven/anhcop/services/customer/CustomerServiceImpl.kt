package com.dogsteven.anhcop.services.customer

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CustomerServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): CustomerService {
    override fun execute(command: CustomerCommand.Register): CustomerCommand.Register.Response {
        if (userRepository.findByUsername(command.username) != null) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "User with username \"${command.username}\" already exists"
            )
        }

        val profile = User.Customer(
            username = command.username,
            password = passwordEncoder.encode(command.password),
            name = command.name,
            phone = command.phone
        ).let(userRepository::save).model

        return CustomerCommand.Register.Response(
            profile = profile
        )
    }
}