package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.services.employee.EmployeeCommand
import com.dogsteven.anhcop.services.employee.EmployeeService
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/employee")
class EmployeeController(
    private val employeeService: EmployeeService
) {
    @PostMapping
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun createEmployee(@RequestBody command: EmployeeCommand.CreateEmployee): EmployeeCommand.CreateEmployee.Response {
        return employeeService.execute(command)
    }

    @PutMapping
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun assignEmployeeToVendor(
        @RequestBody command: EmployeeCommand.AssignEmployeeToVendor
    ): EmployeeCommand.AssignEmployeeToVendor.Response {
        return employeeService.execute(command)
    }
}