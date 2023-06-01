package com.dogsteven.anhcop.services.product

import com.dogsteven.anhcop.entities.Product
import com.dogsteven.anhcop.repositories.ProductRepository
import com.dogsteven.anhcop.utils.BufferedImageUtils.Companion.centerCrop
import com.dogsteven.anhcop.utils.BufferedImageUtils.Companion.scaleTo
import com.dogsteven.anhcop.utils.BufferedImageUtils.Companion.toByteArray
import com.dogsteven.anhcop.utils.ValidatorExtension.Companion.throwValidate
import jakarta.validation.Validator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import javax.imageio.ImageIO

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val validator: Validator
): ProductService {
    override fun execute(command: ProductCommand.GetAllProducts): ProductCommand.GetAllProducts.Response {

        val products = productRepository.findAll().map(Product::model)

        return ProductCommand.GetAllProducts.Response(
            products = products
        )
    }

    override fun execute(command: ProductCommand.GetProductImageById): ProductCommand.GetProductImageById.Response {
        val product = productRepository.findByIdOrNull(command.id)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Product with id \"${command.id}\" does not exist"
            )

        return ProductCommand.GetProductImageById.Response(
            stream = product.image.inputStream()
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
            prices = metadata.prices,
            image = ImageIO.read(command.image)
                .centerCrop()
                .scaleTo(300, 300)
                .toByteArray()
        ).let(productRepository::save)

        return ProductCommand.CreateProduct.Response(
            id = product.id!!
        )
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
            product.image = ImageIO.read(command.image)
                .centerCrop()
                .scaleTo(300, 300)
                .toByteArray()
        }

        return ProductCommand.UpdateProduct.Response
    }

    override fun execute(command: ProductCommand.DeleteProduct): ProductCommand.DeleteProduct.Response {
        if (!productRepository.existsById(command.id)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Product with id \"${command.id}\" does not exist"
            )
        }

        productRepository.deleteById(command.id)

        return ProductCommand.DeleteProduct.Response
    }
}