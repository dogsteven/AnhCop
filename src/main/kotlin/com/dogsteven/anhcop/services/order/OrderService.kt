package com.dogsteven.anhcop.services.order

import org.springframework.transaction.annotation.Transactional

interface OrderService {
    fun execute(command: OrderCommand.StreamOrders): OrderCommand.StreamOrders.Response

    @Transactional
    fun execute(command: OrderCommand.CreateOrder): OrderCommand.CreateOrder.Response

    @Transactional
    fun execute(command: OrderCommand.DeleteOrdersBetween): OrderCommand.DeleteOrdersBetween.Response
}