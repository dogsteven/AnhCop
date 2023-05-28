package com.dogsteven.anhcop.services.order

import com.dogsteven.anhcop.entities.Order
import com.dogsteven.anhcop.entities.OrderItem
import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.repositories.OrderItemRepository
import com.dogsteven.anhcop.repositories.OrderRepository
import com.dogsteven.anhcop.repositories.ProductRepository
import com.dogsteven.anhcop.services.order.order_channel.OrderChannel
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.OffsetDateTime

@Service
class OrderServiceImpl(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val orderChannel: OrderChannel
): OrderService {
    override fun execute(command: OrderCommand.StreamOrders): OrderCommand.StreamOrders.Response {
        val streaming = orderChannel.flux()

        return OrderCommand.StreamOrders.Response(
            flux = streaming.map { order ->
                ServerSentEvent.builder<Order.Model>()
                    .id(order.id.toString())
                    .event("order-committed")
                    .data(order)
                    .build()
            }
        )
    }

    override fun execute(command: OrderCommand.CreateOrder): OrderCommand.CreateOrder.Response {
        val createdDateTime = OffsetDateTime.now()

        val createdUser = (command.principal.user as? User.Employee)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Only employee can create an order"
            )

        val createdVendor = createdUser.workingVendor
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "You have not worked at any vendor yet"
            )

        val order = Order(
            createdDateTime = createdDateTime,
            createdUser = createdUser,
            createdVendor = createdVendor
        ).let(orderRepository::save).apply {
            for (item in command.metadata.items) {
                val product = productRepository.findByIdOrNull(item.productId)
                    ?: throw ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Product with id \"${item.productId}\" does not exist"
                    )

                if (!product.prices.contains(item.price)) {
                    throw ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Product \"${product.name}\" does not have price ${item.price}"
                    )
                }

                add(
                    OrderItem(
                        product = product,
                        price = item.price,
                        quantity = item.quantity
                    ).let(orderItemRepository::save)
                )
            }
        }.let(orderRepository::save)

        orderChannel.broadcast(order.model)

        return OrderCommand.CreateOrder.Response
    }

    override fun execute(command: OrderCommand.DeleteOrdersBetween): OrderCommand.DeleteOrdersBetween.Response {
        val orders = orderRepository.findAllByCreatedDateTimeBetween(
            startDateTime = command.startDateTime,
            endDateTime = command.endDateTime
        )

        for (order in orders) {
            orderRepository.delete(order)
        }

        return OrderCommand.DeleteOrdersBetween.Response
    }
}