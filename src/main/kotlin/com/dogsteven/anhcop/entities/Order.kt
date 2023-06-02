package com.dogsteven.anhcop.entities

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant
import java.util.*

@Entity
@Table(name = "t_order")
class Order(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(name = "created_date_time")
    var createdDateTime: Instant,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user_id")
    var createdUser: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_vendor_id")
    var createdVendor: Vendor,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var orderAndOrderItems: List<OrderAndOrderItem> = listOf()
) {
    val items: List<OrderItem>
        @Transient
        get() = orderAndOrderItems.map(OrderAndOrderItem::orderItem)

    fun add(orderItem: OrderItem) {
        orderAndOrderItems = orderAndOrderItems.plus(
            OrderAndOrderItem(
                order = this,
                orderItem = orderItem
            )
        )
    }

    val model: Model
        @Transient
        get() = Model(
            id = id ?: throw RuntimeException("Field 'Order::id' is null"),
            createdDateTime = createdDateTime,
            createdUserId = createdUser.id ?: throw RuntimeException("Field 'Order::createdUser::id' is null"),
            createdVendorId = createdVendor.id ?: throw RuntimeException("Field 'Order::createdVendor::id' is null"),
            items = items.map(OrderItem::model)
        )

    class Model(
        val id: Long,
        val createdDateTime: Instant,
        val createdUserId: Long,
        val createdVendorId: Long,
        val items: List<OrderItem.Model>
    )

    @Embeddable
    class OrderAndOrderItemId(
        @Column(name = "order_id")
        var orderId: Long?,
        @Column(name = "order_item_id")
        var orderItemId: Long?
    ): Serializable {
        override fun equals(other: Any?): Boolean {
            if (other !is OrderAndOrderItemId) {
                return false
            }

            return other.orderId == orderId && other.orderItemId == orderItemId
        }

        override fun hashCode(): Int {
            return Objects.hash(orderId, orderItemId)
        }
    }

    @Entity
    @Table(
        name = "t_order_and_order_item",
        indexes = [
            Index(columnList = "order_id")
        ]
    )
    class OrderAndOrderItem(
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @MapsId("orderId")
        var order: Order,
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @MapsId("orderItemId")
        var orderItem: OrderItem
    ) {
        @EmbeddedId
        private val id: OrderAndOrderItemId = OrderAndOrderItemId(
            orderId = order.id,
            orderItemId = orderItem.id
        )
    }
}