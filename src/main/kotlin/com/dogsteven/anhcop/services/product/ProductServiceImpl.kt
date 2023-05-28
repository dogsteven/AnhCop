package com.dogsteven.anhcop.services.product

import com.dogsteven.anhcop.entities.Product
import com.dogsteven.anhcop.repositories.ProductRepository
import com.dogsteven.anhcop.services.image.ImageCommand
import com.dogsteven.anhcop.services.image.ImageService
import com.dogsteven.anhcop.utils.ValidatorExtension.Companion.throwValidate
import jakarta.validation.Validator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val validator: Validator,
    private val imageService: ImageService
): ProductService {
    override fun execute(command: ProductCommand.GetAllProducts): ProductCommand.GetAllProducts.Response {
        val products = productRepository.findAll().map(Product::model)

        return ProductCommand.GetAllProducts.Response(
            products = products
        )
    }

    override fun execute(command: ProductCommand.CreateProduct): ProductCommand.CreateProduct.Response {
        val metadata = command.metadata
        validator.throwValidate(metadata)

        if (productRepository.findByName(metadata.name) != null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Product with name \"${metadata.name}\" already exists"
            )
        }

        val product = Product(
            name = metadata.name,
            prices = metadata.prices
        ).let(productRepository::save)

        return ProductCommand.CreateProduct.Response(
            id = product.id!!
        ).apply {
            imageService.execute(
                ImageCommand.Store(
                    fileName = "product-$id.png",
                    fileStream = command.image
                )
            )
        }
    }

    override fun execute(command: ProductCommand.UpdateProduct): ProductCommand.UpdateProduct.Response {
        val metadata = command.metadata

        validator.throwValidate(metadata)

        val product = productRepository.findByIdOrNull(metadata.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Product with id \"${metadata.id}\" does not exist"
            )

        if (metadata.name != null) {
            if (productRepository.findByName(metadata.name) != null) {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Product with name \"${metadata.name}\" already exists"
                )
            }

            product.name = metadata.name
        }

        if (metadata.prices != null) {
            product.prices = metadata.prices
        }

        productRepository.save(product)

        if (command.image != null) {
            imageService.execute(
                ImageCommand.Store(
                    fileName = "product-${metadata.id}.png",
                    fileStream = command.image
                )
            )
        }

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

        imageService.execute(
            ImageCommand.Remove(
                fileName = "product-${command.id}.png"
            )
        )

        return ProductCommand.DeleteProduct.Response
    }
}