package com.dogsteven.anhcop.repositories

import com.dogsteven.anhcop.entities.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.OffsetDateTime

@Repository
interface OrderRepository: JpaRepository<Order, Long> {
    fun findAllByCreatedDateTimeBetween(startDateTime: Instant, endDateTime: Instant): Collection<Order>
}