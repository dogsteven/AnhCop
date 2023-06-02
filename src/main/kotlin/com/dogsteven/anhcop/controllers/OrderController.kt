package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.entities.Order
import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.services.order.OrderCommand
import com.dogsteven.anhcop.services.order.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import java.time.Instant
import java.time.OffsetDateTime

@RestController
@RequestMapping("/api/order")
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @Secured("ROLE_ADMINISTRATOR")
    fun streamOrders(): Flux<ServerSentEvent<Order.Model>> {
        val command = OrderCommand.StreamOrders
        val response = orderService.execute(command)
        return response.flux
    }

    @PostMapping
    @ResponseBody
    @Secured("ROLE_EMPLOYEE")
    fun createOrder(authentication: Authentication, @RequestBody metadata: OrderCommand.CreateOrder.Metadata): OrderCommand.CreateOrder.Response {
        val principal = (authentication.principal as? User.Principal)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unsupported principal type"
            )

        val command = OrderCommand.CreateOrder(principal, metadata)

        return orderService.execute(command)
    }

    @DeleteMapping("/{startDateTime}/{endDateTime}")
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun deleteOrdersBetween(
        @PathVariable("startDateTime") startDateTime: Instant,
        @PathVariable("endDateTime") endDateTime: Instant
    ): OrderCommand.DeleteOrdersBetween.Response {
        val command = OrderCommand.DeleteOrdersBetween(startDateTime, endDateTime)

        return orderService.execute(command)
    }
}