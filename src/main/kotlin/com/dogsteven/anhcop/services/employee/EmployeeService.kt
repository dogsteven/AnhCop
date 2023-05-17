package com.dogsteven.anhcop.services.employee

import org.springframework.transaction.annotation.Transactional

interface EmployeeService {
    @Transactional
    fun execute(command: EmployeeCommand.CreateEmployee): EmployeeCommand.CreateEmployee.Response

    @Transactional
    fun execute(command: EmployeeCommand.AssignEmployeeToVendor): EmployeeCommand.AssignEmployeeToVendor.Response
}