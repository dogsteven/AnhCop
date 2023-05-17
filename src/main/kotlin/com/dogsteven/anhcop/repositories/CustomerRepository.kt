package com.dogsteven.anhcop.repositories

import com.dogsteven.anhcop.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<User.Customer, Long> {
    fun findByUsername(username: String): User.Customer?
}