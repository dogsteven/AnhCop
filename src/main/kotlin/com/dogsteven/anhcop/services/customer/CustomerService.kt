package com.dogsteven.anhcop.services.customer

import org.springframework.transaction.annotation.Transactional

interface CustomerService {
    @Transactional
    fun execute(command: CustomerCommand.Register): CustomerCommand.Register.Response
}