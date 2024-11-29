package com.dropesac.app.data.response

import com.dropesac.app.data.model.Product

data class ProductResponse(
    val success: Boolean,
    val products: List<Product>
)
