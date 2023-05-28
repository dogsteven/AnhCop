package com.dogsteven.anhcop.services.order.order_channel

import com.dogsteven.anhcop.entities.Order
import reactor.core.publisher.Flux

interface OrderChannel {
    fun flux(): Flux<Order.Model>

    fun broadcast(order: Order.Model)
}