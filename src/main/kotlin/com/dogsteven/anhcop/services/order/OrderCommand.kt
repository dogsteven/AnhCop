package com.dogsteven.anhcop.services.order

import com.dogsteven.anhcop.entities.Order
import com.dogsteven.anhcop.entities.OrderItem
import com.dogsteven.anhcop.entities.User
import org.springframework.http.codec.ServerSentEvent
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object OrderCommand {
    object StreamOrders {
        class Response(
            val flux: Flux<ServerSentEvent<Order.Model>>
        )
    }

    class StreamOrdersFrom(
        val dateTime: LocalDateTime
    ) {
        class Response(
            val flux: Flux<ServerSentEvent<Order.Model>>
        )
    }

    class CreateOrder(
        val principal: User.Principal,
        val metadata: Metadata
    ) {
        class Metadata(
            val items: Collection<OrderItem.Model>
        )

        object Response
    }

    class DeleteOrdersBetween(
        val startDate: LocalDate,
        val endDate: LocalDate
    ) {
        object Response
    }
}