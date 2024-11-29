package com.dropesac.app.data.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.dropesac.app.data.model.Product
import com.dropesac.app.data.response.ProductResponse
import org.json.JSONException
import org.json.JSONObject

class ProductRepository(context: Context) : BaseRepository<Product>(
    context = context,
    apiUrl = "https://adratech.app/dropesac/search_products.php",
    clazz = Product::class.java
) {
    fun getByQuery(
        query: String = " ",
        listener: Response.Listener<List<Product>>,
        errorListener: Response.ErrorListener
    ) {
        val jsonParams = JSONObject()

        try {
            jsonParams.put("query", query)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            apiUrl,
            jsonParams,
            { response ->
                val productResponse = parseJsonToObject(response, ProductResponse::class.java)
                listener.onResponse(productResponse.products)
            },
            errorListener
        )

        requestQueue.add(request)
    }

    private fun parseJsonToObject(jsonObject: JSONObject, clazz: Class<ProductResponse>): ProductResponse {
        return gson.fromJson(jsonObject.toString(), clazz)
    }
}
