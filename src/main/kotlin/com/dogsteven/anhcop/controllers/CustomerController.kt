package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.services.customer.CustomerCommand
import com.dogsteven.anhcop.services.customer.CustomerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customer")
class CustomerController(
    private val customerService: CustomerService,
) {
    @PostMapping("/register")
    @ResponseBody
    fun register(
        @RequestBody command: CustomerCommand.Register
    ): CustomerCommand.Register.Response {
        return customerService.execute(command)
    }
}