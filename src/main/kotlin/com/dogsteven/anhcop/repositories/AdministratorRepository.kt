package com.dogsteven.anhcop.repositories

import com.dogsteven.anhcop.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdministratorRepository: JpaRepository<User.Administrator, Long> {
    fun findByUsername(username: String): User.Administrator?
}