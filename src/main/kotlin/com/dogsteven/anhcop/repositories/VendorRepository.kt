package com.dogsteven.anhcop.repositories

import com.dogsteven.anhcop.entities.Vendor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VendorRepository: JpaRepository<Vendor, Long> {
    fun findByName(name: String): Vendor?

    fun existsByName(name: String): Boolean
}