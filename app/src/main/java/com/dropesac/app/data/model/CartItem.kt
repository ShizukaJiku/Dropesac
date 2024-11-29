package com.dropesac.app.data.model

data class CartItem(
    val productId: Int,
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val productImageUrl: String,
    val quantity: Int
) {
    fun getSubtotal(): Double {
        return productPrice * quantity
    }
}
