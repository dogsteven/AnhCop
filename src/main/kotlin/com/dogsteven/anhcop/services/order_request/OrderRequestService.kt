package com.dogsteven.anhcop.services.order_request

import org.springframework.transaction.annotation.Transactional

interface OrderRequestService {
    fun execute(command: OrderRequestCommand.StreamOrderRequests): OrderRequestCommand.StreamOrderRequests.Response

    @Transactional
    fun execute(command: OrderRequestCommand.MakeOrderRequest): OrderRequestCommand.MakeOrderRequest.Response

    @Transactional
    fun execute(command: OrderRequestCommand.AcceptOrderRequest): OrderRequestCommand.AcceptOrderRequest.Response

    @Transactional
    fun execute(command: OrderRequestCommand.RejectOrderRequest): OrderRequestCommand.RejectOrderRequest.Response

    @Transactional
    fun execute(command: OrderRequestCommand.ShippingOrderRequest): OrderRequestCommand.ShippingOrderRequest.Response

    @Transactional
    fun execute(command: OrderRequestCommand.CompleteOrderRequest): OrderRequestCommand.CompleteOrderRequest.Response
}