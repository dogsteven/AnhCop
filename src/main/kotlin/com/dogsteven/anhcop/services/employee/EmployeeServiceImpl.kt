package com.dogsteven.anhcop.services.employee

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.repositories.EmployeeRepository
import com.dogsteven.anhcop.repositories.UserRepository
import com.dogsteven.anhcop.repositories.VendorRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class EmployeeServiceImpl(
    private val vendorRepository: VendorRepository,
    private val userRepository: UserRepository,
    private val employeeRepository: EmployeeRepository
): EmployeeService {
    override fun execute(command: EmployeeCommand.CreateEmployee): EmployeeCommand.CreateEmployee.Response {
        if (userRepository.findByUsername(command.username) != null) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "User with username \"${command.username}\" already exists"
            )
        }

        val employee = User.Employee(
            username = command.username,
            password = command.password,
            name = command.name,
            phone = command.phone
        ).let(userRepository::save)

        return EmployeeCommand.CreateEmployee.Response(
            id = employee.id!!
        )
    }

    override fun execute(command: EmployeeCommand.AssignEmployeeToVendor): EmployeeCommand.AssignEmployeeToVendor.Response {
        val employee = employeeRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Employee with id \"${command.id}\" does not exist"
            )

        if (command.vendorId == null) {
            employee.workingVendor = null
        } else {
            val vendor = vendorRepository.findByIdOrNull(command.vendorId)
                ?: throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vendor with id \"${command.vendorId}\" does not exist"
                )

            employee.workingVendor = vendor
        }

        employeeRepository.save(employee)

        return EmployeeCommand.AssignEmployeeToVendor.Response
    }
}