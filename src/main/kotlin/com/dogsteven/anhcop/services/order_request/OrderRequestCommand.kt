package com.dogsteven.anhcop.services.order_request

import com.dogsteven.anhcop.entities.OrderItem
import com.dogsteven.anhcop.entities.OrderRequest
import com.dogsteven.anhcop.entities.User
import org.springframework.http.codec.ServerSentEvent
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux

object OrderRequestCommand {
    object StreamOrderRequests {
        class Response(
            val flux: Flux<ServerSentEvent<OrderRequest.Model>>
        )
    }

    class MakeOrderRequest(
        val principal: User.Principal,
        val metadata: Metadata
    ) {
        class Metadata(
            val items: Collection<OrderItem.Model>
        )

        object Response
    }

    class AcceptOrderRequest(
        val id: Long,
        val vendorId: Long
    ) {
        object Response
    }

    class RejectOrderRequest(
        val id: Long
    ) {
        object Response
    }

    class ShippingOrderRequest(
        val id: Long
    ) {
        object Response
    }

    class CompleteOrderRequest(
        val id: Long
    ) {
        object Response
    }
}