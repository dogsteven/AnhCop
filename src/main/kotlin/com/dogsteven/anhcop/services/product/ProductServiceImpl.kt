package com.dogsteven.anhcop.services.product

import com.dogsteven.anhcop.entities.Product
import com.dogsteven.anhcop.repositories.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
): ProductService {
    override fun execute(command: ProductCommand.GetAllProducts): ProductCommand.GetAllProducts.Response {
        val products = productRepository.findAll().map(Product::model)

        return ProductCommand.GetAllProducts.Response(
            products = products
        )
    }

    override fun execute(command: ProductCommand.CreateProduct): ProductCommand.CreateProduct.Response {
        if (productRepository.findByName(command.name) != null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Product with name \"${command.name}\" already exists"
            )
        }

        val product = Product(
            name = command.name,
            prices = command.prices
        ).let(productRepository::save)

        return ProductCommand.CreateProduct.Response(
            id = product.id!!
        )
    }

    override fun execute(command: ProductCommand.UpdateProduct): ProductCommand.UpdateProduct.Response {
        val product = productRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Product with id \"${command.id}\" does not exist"
            )

        if (command.name != null) {
            product.name = command.name
        }

        if (command.prices != null) {
            product.prices = command.prices
        }

        productRepository.save(product)

        return ProductCommand.UpdateProduct.Response
    }

    override fun execute(command: ProductCommand.DeleteProduct): ProductCommand.DeleteProduct.Response {
        if (productRepository.findByIdOrNull(command.id) == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Product with id \"${command.id}\" does not exist"
            )
        }

        productRepository.deleteById(command.id)

        return ProductCommand.DeleteProduct.Response
    }
}