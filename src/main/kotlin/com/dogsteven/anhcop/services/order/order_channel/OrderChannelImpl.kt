package com.dogsteven.anhcop.services.order.order_channel

import com.dogsteven.anhcop.entities.Order
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Component
class OrderChannelImpl: OrderChannel {
    private val sink = Sinks
        .many()
        .multicast()
        .directAllOrNothing<Order.Model>()

    override val flux: Flux<Order.Model>
        get() = sink.asFlux()

    @Async
    override fun broadcast(order: Order.Model) {
        val emitResult = sink.tryEmitNext(order)

        if (emitResult == Sinks.EmitResult.FAIL_OVERFLOW) {
            broadcast(order)
        }
    }
}