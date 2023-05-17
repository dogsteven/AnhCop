package com.dogsteven.anhcop.entities

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Objects

@Entity
@Table(name = "t_order_request")
class OrderRequest(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Enumerated(EnumType.STRING)
    var state: State = State.PENDING,
    @Column(name = "created_date_time")
    var createdDateTime: LocalDateTime,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_customer_id")
    var createdCustomer: User.Customer,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderRequest", cascade = [CascadeType.ALL], orphanRemoval = true)
    var orderRequestAndOrderItems: List<OrderRequestAndOrderItem> = listOf()
) {
    val items: List<OrderItem>
        @Transient
        get() = orderRequestAndOrderItems.map(OrderRequestAndOrderItem::orderItem)

    fun add(orderItem: OrderItem) {
        orderRequestAndOrderItems = orderRequestAndOrderItems.plus(
            OrderRequestAndOrderItem(
                orderRequest = this,
                orderItem = orderItem
            )
        )
    }

    enum class State {
        PENDING, ACCEPTED, SHIPPING
    }

    val model: Model
        @Transient
        get() = Model(
            id = id ?: throw RuntimeException("Field 'OrderRequest::id' is null"),
            state = state,
            createdDateTime = createdDateTime,
            createdCustomerId = createdCustomer.id ?: throw RuntimeException("Field 'OrderRequest::createdCustomer::id' is null"),
            items = items.map(OrderItem::model)
        )

    class Model(
        val id: Long,
        val state: State,
        val createdDateTime: LocalDateTime,
        val createdCustomerId: Long,
        val items: List<OrderItem.Model>
    )

    @Embeddable
    class OrderRequestAndOrderItemId(
        @Column(name = "order_request_id")
        var orderRequestId: Long?,
        @Column(name = "order_item_id")
        var orderItemId: Long?
    ): Serializable {
        override fun equals(other: Any?): Boolean {
            if (other !is OrderRequestAndOrderItemId) {
                return false
            }

            return other.orderRequestId == orderRequestId && other.orderItemId == orderItemId
        }

        override fun hashCode(): Int {
            return Objects.hash(orderRequestId, orderItemId)
        }
    }

    @Entity
    @Table(
        name = "t_order_request_and_order_item",
        indexes = [
            Index(columnList = "order_request_id")
        ]
    )
    class OrderRequestAndOrderItem(
        @OnDelete(action = OnDeleteAction.CASCADE)
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @MapsId("orderRequestId")
        var orderRequest: OrderRequest,
        @OnDelete(action = OnDeleteAction.CASCADE)
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @MapsId("orderItemId")
        var orderItem: OrderItem
    ) {
        @EmbeddedId
        private var id: OrderRequestAndOrderItemId = OrderRequestAndOrderItemId(
            orderRequestId = orderRequest.id,
            orderItemId = orderItem.id
        )
    }
}