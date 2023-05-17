package com.dogsteven.anhcop.repositories

import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.entities.Vendor
import org.springframework.data.jpa.repository.JpaRepository

interface EmployeeRepository: JpaRepository<User.Employee, Long> {
    fun findByUsername(username: String): User.Employee?

    fun findAllByWorkingVendor(workingVendor: Vendor): Collection<User.Employee>
}