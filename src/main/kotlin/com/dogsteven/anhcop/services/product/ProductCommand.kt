package com.dogsteven.anhcop.services.product

import com.dogsteven.anhcop.entities.Product

object ProductCommand {
    object GetAllProducts {
        class Response(
            val products: Collection<Product.Model>
        )
    }

    class CreateProduct(
        val name: String,
        val prices: Set<Int>
    ) {
        class Response(
            val id: Long
        )
    }

    class UpdateProduct(
        val id: Long,
        val name: String?,
        val prices: Set<Int>?
    ) {
        object Response
    }

    class DeleteProduct(
        val id: Long
    ) {
        object Response
    }
}