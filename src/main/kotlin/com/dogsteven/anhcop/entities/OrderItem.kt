package com.dogsteven.anhcop.entities

import jakarta.persistence.*


@Entity
@Table(name = "t_order_item")
class OrderItem(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product,
    @Column
    var price: Int,
    @Column
    var quantity: Int
) {
    val model: Model
        @Transient
        get() = Model(
            productId = product.id ?: throw RuntimeException("Field 'OrderItem::product::id' is null"),
            price = price,
            quantity = quantity
        )

    class Model(
        val productId: Long,
        val price: Int,
        val quantity: Int
    )
}