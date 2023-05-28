package com.dogsteven.anhcop.services.order.order_channel

import com.dogsteven.anhcop.entities.Order
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Component
class OrderChannelImpl: OrderChannel {
    private val sink: Sinks.Many<Order.Model> = Sinks
        .many()
        .multicast()
        .onBackpressureBuffer()

    override fun flux(): Flux<Order.Model> = sink.asFlux()

    @Async
    override fun broadcast(order: Order.Model) {
        sink.tryEmitNext(order)
    }
}