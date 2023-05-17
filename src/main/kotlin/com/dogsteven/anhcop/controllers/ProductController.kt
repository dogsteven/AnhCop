package com.dogsteven.anhcop.controllers

import com.dogsteven.anhcop.entities.Product
import com.dogsteven.anhcop.services.product.ProductCommand
import com.dogsteven.anhcop.services.product.ProductService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

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

    @PostMapping
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun createProduct(@RequestBody command: ProductCommand.CreateProduct): ProductCommand.CreateProduct.Response {
        return productService.execute(command)
    }

    @PutMapping
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun updateProduct(@RequestBody command: ProductCommand.UpdateProduct): ProductCommand.UpdateProduct.Response {
        return productService.execute(command)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Secured("ROLE_ADMINISTRATOR")
    fun deleteProduct(@PathVariable("id") id: Long): ProductCommand.DeleteProduct.Response {
        val command = ProductCommand.DeleteProduct(id)

        return productService.execute(command)
    }
}