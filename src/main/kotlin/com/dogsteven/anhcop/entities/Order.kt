package com.dogsteven.anhcop.entities

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(
    name = "t_order",
    indexes = [
        Index(columnList = "created_vendor_id")
    ]
)
class Order(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(name = "created_date_time")
    var createdDateTime: LocalDateTime,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user_id")
    var createdUser: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_vendor_id")
    var createdVendor: Vendor,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
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
        val createdDateTime: LocalDateTime,
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
    ): Serializable

    @Entity
    @Table(
        name = "t_order_and_order_item",
        indexes = [
            Index(columnList = "order_id")
        ]
    )
    class OrderAndOrderItem(
        @OnDelete(action = OnDeleteAction.CASCADE)
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @MapsId("orderId")
        var order: Order,
        @OnDelete(action = OnDeleteAction.CASCADE)
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