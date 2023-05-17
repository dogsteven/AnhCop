package com.dogsteven.anhcop.repositories

import com.dogsteven.anhcop.entities.Order
import com.dogsteven.anhcop.entities.Vendor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface OrderRepository: JpaRepository<Order, Long> {
    fun findAllByCreatedDateTimeAfter(time: LocalDateTime): Collection<Order>

    fun findAllByCreatedVendorAndCreatedDateTimeBetween(vendor: Vendor, startDateTime: LocalDateTime, endDateTime: LocalDateTime): Collection<Order>

    fun findAllByCreatedDateTimeBetween(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Collection<Order>
}