package com.dogsteven.anhcop.services.order_request.order_request_channel

import com.dogsteven.anhcop.entities.OrderRequest
import reactor.core.publisher.Flux

interface OrderRequestChannel {
    val flux: Flux<OrderRequest.Model>

    fun broadcast(orderRequest: OrderRequest.Model)
}