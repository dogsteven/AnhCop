package com.dogsteven.anhcop.services.order_request.order_request_channel

import com.dogsteven.anhcop.entities.OrderRequest
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Component
class OrderRequestChannelImpl: OrderRequestChannel {
    private val sink = Sinks
        .many()
        .multicast()
        .directAllOrNothing<OrderRequest.Model>()

    override val flux: Flux<OrderRequest.Model>
        get() = sink.asFlux()

    @Async
    override fun broadcast(orderRequest: OrderRequest.Model) {
        val emitResult = sink.tryEmitNext(orderRequest)

        if (emitResult == Sinks.EmitResult.FAIL_OVERFLOW) {
            broadcast(orderRequest)
        }
    }
}