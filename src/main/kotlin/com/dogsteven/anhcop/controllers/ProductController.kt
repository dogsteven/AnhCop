package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.services.product.ProductCommand
import com.dogsteven.anhcop.services.product.ProductService
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/product")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    @ResponseBody
    fun getAllProducts(): ProductCommand.GetAllProducts.Response {
        val command = ProductCommand.GetAllProducts

        return productService.execute(command)
    }

    @GetMapping("/{id}/image", produces = [MediaType.IMAGE_PNG_VALUE])
    fun streamProductImage(@PathVariable("id") id: Long): ResponseEntity<Resource> {
        val command = ProductCommand.GetProductImageById(id)
        val stream = productService.execute(command).stream
        val resource = InputStreamResource(stream)
        return ResponseEntity.ofNullable(resource)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun createProduct(
        @RequestPart(name = "metadata") metadata: ProductCommand.CreateProduct.Metadata,
        @RequestPart(name = "image") imagePart: MultipartFile
    ): ProductCommand.CreateProduct.Response {
        val command = ProductCommand.CreateProduct(
            metadata = metadata,
            image = imagePart.inputStream
        )
        return productService.execute(command)
    }

    @PutMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun updateProduct(
        @RequestPart(name = "metadata") metadata: ProductCommand.UpdateProduct.Metadata,
        @RequestPart(name = "image", required = false) imagePart: MultipartFile?
    ): ProductCommand.UpdateProduct.Response {
        val command = ProductCommand.UpdateProduct(
            metadata = metadata,
            image = imagePart?.inputStream
        )
        return productService.execute(command)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    @Transactional
    fun deleteProduct(@PathVariable("id") id: Long): ProductCommand.DeleteProduct.Response {
        val command = ProductCommand.DeleteProduct(id)
        return productService.execute(command)
    }
}