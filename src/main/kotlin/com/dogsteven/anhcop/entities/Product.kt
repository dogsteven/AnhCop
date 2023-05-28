package com.dogsteven.anhcop.entities

import jakarta.persistence.*

@Entity
@Table(name = "t_product")
class Product(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(unique = true)
    var name: String,
    @ElementCollection
    var prices: Set<Int>
) {
    val model: Model
        @Transient
        get() = Model(
            id = id ?: throw RuntimeException("Field 'Product::id' is null"),
            name = name,
            prices = prices
        )

    class Model(
        val id: Long,
        val name: String,
        val prices: Set<Int>
    )
}