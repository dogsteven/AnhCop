package com.dogsteven.anhcop.services.customer

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.repositories.UserRepository
import com.dogsteven.anhcop.utils.ValidatorExtension.Companion.throwValidate
import jakarta.validation.Validator
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CustomerServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val validator: Validator
): CustomerService {
    override fun execute(command: CustomerCommand.Register): CustomerCommand.Register.Response {
        validator.throwValidate(command)

        if (userRepository.existsByUsername(command.name)) {
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