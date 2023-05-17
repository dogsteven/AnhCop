package com.dogsteven.anhcop.services.employee

object EmployeeCommand {
    class CreateEmployee(
        val username: String,
        val password: String,
        val name: String,
        val phone: String
    ) {
        class Response(
            val id: Long
        )
    }

    class AssignEmployeeToVendor(
        val id: Long,
        val vendorId: Long?
    ) {
        object Response
    }
}