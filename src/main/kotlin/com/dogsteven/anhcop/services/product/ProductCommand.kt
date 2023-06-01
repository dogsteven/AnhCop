package com.dogsteven.anhcop.services.product

import com.dogsteven.anhcop.entities.Product
import com.dogsteven.anhcop.utils.NullOrNotBlank
import com.dogsteven.anhcop.utils.PositiveElements
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.io.InputStream

object ProductCommand {
    object GetAllProducts {
        class Response(
            val products: Collection<Product.Model>
        )
    }

    class GetProductById(
        val id: Long
    ) {
        class Response(
            val product: Product.Model
        )
    }

    class CreateProduct(
        val metadata: Metadata,
        val image: InputStream
    ) {
        class Metadata(
            @field:NotBlank(message = "Name must not be blank")
            val name: String,
            @field:Size(min = 1, message = "Prices must have at least one element")
            @field:PositiveElements(message = "A price must be positive")
            val prices: Set<Int>
        )

        class Response(
            val id: Long
        )
    }

    class UpdateProduct(
        val metadata: Metadata,
        val image: InputStream?
    ) {
        class Metadata(
            val id: Long,
            @field:NullOrNotBlank(message = "Name must not be blank")
            val name: String?,
            @field:Size(min = 1, message = "Prices must have at least one element")
            @field:PositiveElements(message = "A price must be positive")
            val prices: Set<Int>?
        )

        object Response
    }

    class DeleteProduct(
        val id: Long
    ) {
        object Response
    }
}