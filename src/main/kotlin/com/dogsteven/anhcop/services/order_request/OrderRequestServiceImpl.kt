package com.dogsteven.anhcop.services.order_request

import com.dogsteven.anhcop.entities.OrderItem
import com.dogsteven.anhcop.entities.OrderRequest
import com.dogsteven.anhcop.entities.User
import com.dogsteven.anhcop.repositories.*
import com.dogsteven.anhcop.services.order_request.order_request_channel.OrderRequestChannel
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class OrderRequestServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderRequestRepository: OrderRequestRepository,
    private val orderItemRepository: OrderItemRepository,
    private val vendorRepository: VendorRepository,
    private val productRepository: ProductRepository,
    private val orderRequestChannel: OrderRequestChannel
): OrderRequestService {
    override fun execute(command: OrderRequestCommand.StreamOrderRequests): OrderRequestCommand.StreamOrderRequests.Response {
        return OrderRequestCommand.StreamOrderRequests.Response(
            flux = orderRequestChannel.flux.map { orderRequest ->
                ServerSentEvent.builder<OrderRequest.Model>()
                    .id(orderRequest.id.toString())
                    .event("order-request-created")
                    .data(orderRequest)
                    .build()
            }
        )
    }

    override fun execute(command: OrderRequestCommand.MakeOrderRequest): OrderRequestCommand.MakeOrderRequest.Response {
        val customer = (command.principal.user as? User.Customer)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Wrong role"
            )

        val metadata = command.metadata

        val orderRequest = OrderRequest(
            createdDateTime = LocalDateTime.now(),
            createdCustomer = customer
        ).let(orderRequestRepository::save).apply {
            for (item in metadata.items) {
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
        }.let(orderRequestRepository::save)

        orderRequestChannel.broadcast(orderRequest.model)

        return OrderRequestCommand.MakeOrderRequest.Response
    }

    override fun execute(command: OrderRequestCommand.AcceptOrderRequest): OrderRequestCommand.AcceptOrderRequest.Response {
        TODO()
    }

    override fun execute(command: OrderRequestCommand.RejectOrderRequest): OrderRequestCommand.RejectOrderRequest.Response {
        TODO()
    }

    override fun execute(command: OrderRequestCommand.ShippingOrderRequest): OrderRequestCommand.ShippingOrderRequest.Response {
        TODO()
    }

    override fun execute(command: OrderRequestCommand.CompleteOrderRequest): OrderRequestCommand.CompleteOrderRequest.Response {
        TODO()
    }
}