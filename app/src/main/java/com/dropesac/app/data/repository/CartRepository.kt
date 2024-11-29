package com.dropesac.app.data.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dropesac.app.data.model.CartItem
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import java.net.URLEncoder

class CartRepository(context: Context) {

    private val queue = Volley.newRequestQueue(context)

    fun addToCart(product: CartItem, onResponse: (Boolean, String) -> Unit) {
        val url = "https://adratech.app/dropesac/add_to_cart.php"

        val params = StringBuilder()
        params.append("product_id=")
            .append(URLEncoder.encode(product.productId.toString(), "UTF-8"))
            .append("&product_name=")
            .append(URLEncoder.encode(product.productName, "UTF-8"))
            .append("&product_description=")
            .append(URLEncoder.encode(product.productDescription, "UTF-8"))
            .append("&product_price=")
            .append(URLEncoder.encode(product.productPrice.toString(), "UTF-8"))
            .append("&product_image_url=")
            .append(URLEncoder.encode(product.productImageUrl, "UTF-8"))
            .append("&quantity=")
            .append(URLEncoder.encode(product.quantity.toString(), "UTF-8"))

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if (response.contains("success")) {
                    onResponse(true, "Producto agregado al carrito")
                } else {
                    onResponse(false, "Error al agregar el producto")
                }
            },
            Response.ErrorListener { error: VolleyError ->
                onResponse(false, "Error: ${error.message}")
            }) {

            override fun getBody(): ByteArray {
                return params.toString().toByteArray(Charsets.UTF_8)
            }

            override fun getHeaders(): Map<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                return headers
            }
        }

        // Agregar la solicitud a la cola de Volley
        queue.add(stringRequest)
    }

    // Método para obtener los productos del carrito
    fun getCartItems(onResponse: (List<CartItem>) -> Unit, onError: (String) -> Unit) {
        val url = "https://adratech.app/dropesac/get_cart_items.php"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val success = response.getBoolean("success")
                if (success) {
                    val cartItems = mutableListOf<CartItem>()
                    val itemsArray = response.getJSONArray("cart_items")
                    for (i in 0 until itemsArray.length()) {
                        val item = itemsArray.getJSONObject(i)
                        cartItems.add(
                            CartItem(
                                productId = item.getInt("product_id"),
                                productName = item.getString("product_name"),
                                productDescription = item.getString("product_description"),
                                productPrice = item.getDouble("product_price"),
                                productImageUrl = item.getString("product_image_url"),
                                quantity = item.getInt("quantity")
                            )
                        )
                    }
                    onResponse(cartItems)
                } else {
                    onError("Failed to load cart items.")
                }
            },
            { error ->
                onError("Error: ${error.message}")
            })

        queue.add(jsonRequest)
    }
}
