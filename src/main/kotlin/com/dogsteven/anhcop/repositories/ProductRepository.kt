package com.dogsteven.anhcop.repositories

import com.dogsteven.anhcop.entities.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    @Query("select p.image from Product p where p.id = :id")
    fun getProductImageById(id: Long): ByteArray?

    fun existsByName(name: String): Boolean
}