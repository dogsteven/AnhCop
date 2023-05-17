package com.dogsteven.anhcop.services.product

import org.springframework.transaction.annotation.Transactional

interface ProductService {
    @Transactional
    fun execute(command: ProductCommand.GetAllProducts): ProductCommand.GetAllProducts.Response

    @Transactional
    fun execute(command: ProductCommand.CreateProduct): ProductCommand.CreateProduct.Response

    @Transactional
    fun execute(command: ProductCommand.UpdateProduct): ProductCommand.UpdateProduct.Response

    @Transactional
    fun execute(command: ProductCommand.DeleteProduct): ProductCommand.DeleteProduct.Response
}