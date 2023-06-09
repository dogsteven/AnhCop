package com.dogsteven.anhcop.services.user

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.repositories.UserRepository
import com.dogsteven.anhcop.utils.ValidatorExtension.Companion.throwValidate
import jakarta.validation.Validator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val validator: Validator
): UserService {
    override fun execute(command: UserCommand.GetProfile): UserCommand.GetProfile.Response {
        return UserCommand.GetProfile.Response(
            profile = command.principal.user.model
        )
    }

    override fun execute(command: UserCommand.UpdateProfile): UserCommand.UpdateProfile.Response {
        when (val metadata = command.metadata) {
            is UserCommand.UpdateProfile.Metadata.Administrator -> {
                validator.throwValidate(command)

                val administrator = (command.principal.user as? User.Administrator)
                    ?: throw ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Incompatible data"
                    )

                if (metadata.password != null) {
                    administrator.password = passwordEncoder.encode(metadata.password)
                }

                if (metadata.name != null) {
                    administrator.name = metadata.name
                }

                userRepository.save(administrator)
            }

            is UserCommand.UpdateProfile.Metadata.Employee -> {
                validator.throwValidate(command)

                val employee = (command.principal.user as? User.Employee)
                    ?: throw ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Incompatible data"
                    )

                if (metadata.password != null) {
                    employee.password = passwordEncoder.encode(metadata.password)
                }

                if (metadata.name != null) {
                    employee.name = metadata.name
                }

                if (metadata.phone != null) {
                    employee.phone = metadata.phone
                }

                userRepository.save(employee)
            }

            is UserCommand.UpdateProfile.Metadata.Customer -> {
                validator.throwValidate(command)

                val customer = (command.principal.user as? User.Customer)
                    ?: throw ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Incompatible data"
                    )

                if (metadata.password != null) {
                    customer.password = passwordEncoder.encode(metadata.password)
                }

                if (metadata.name != null) {
                    customer.name = metadata.name
                }

                if (metadata.phone != null) {
                    customer.phone = metadata.phone
                }

                userRepository.save(customer)
            }
        }

        return UserCommand.UpdateProfile.Response
    }

    override fun execute(command: UserCommand.EnableUser): UserCommand.EnableUser.Response {
        val user = userRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User with id \"${command.id}\" does not exist"
            )

        user.enabled = true

        userRepository.save(user)

        return UserCommand.EnableUser.Response
    }

    override fun execute(command: UserCommand.DisableUser): UserCommand.DisableUser.Response {
        val user = userRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User with id \"${command.id}\" does not exist"
            )

        user.enabled = false

        userRepository.save(user)

        return UserCommand.DisableUser.Response
    }
}