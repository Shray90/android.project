package com.example.ai36.repository

import com.example.ai36.model.CartItemModel


interface CartRepository {
    fun addToCart(model: CartItemModel, callback: (Boolean, String) -> Unit)

    fun updateCartItem(cartId: String, data: MutableMap<String, Any?>, callback: (Boolean, String) -> Unit)

    fun removeFromCart(cartId: String, callback: (Boolean, String) -> Unit)

    fun getCartItems(callback: (List<CartItemModel>, Boolean, String) -> Unit)

    fun clearCart(callback: (Boolean, String) -> Unit)

    fun getCartItemByProductId(productId: String, callback: (CartItemModel?, Boolean, String) -> Unit)
}