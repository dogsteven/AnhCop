package com.dogsteven.anhcop.repositories

import com.dogsteven.anhcop.entities.OrderRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRequestRepository: JpaRepository<OrderRequest, Long>