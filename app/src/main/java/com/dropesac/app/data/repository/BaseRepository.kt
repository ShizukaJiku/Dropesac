package com.dropesac.app.data.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject

abstract class BaseRepository<T>(
    private val context: Context,
    protected val apiUrl: String,
    protected val clazz: Class<T>
) {

    protected val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    protected val gson = Gson()

    open fun getAll(
        listener: Response.Listener<List<T>>,
        errorListener: Response.ErrorListener
    ) {
        val request = JsonArrayRequest(
            Request.Method.GET, apiUrl,
            null,
            { response ->
                val list = mutableListOf<T>()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    list.add(parseJsonToObject(jsonObject))
                }
                listener.onResponse(list)
            },
            errorListener
        )
        requestQueue.add(request)
    }

    // Obtener un objeto por su ID
    open fun getById(
        id: Int,
        listener: Response.Listener<T>,
        errorListener: Response.ErrorListener
    ) {
        val url = "$apiUrl/$id"
        val request = JsonObjectRequest(
            Request.Method.GET, url,
            null,
            { response ->
                val obj = parseJsonToObject(response)
                listener.onResponse(obj)
            },
            errorListener
        )
        requestQueue.add(request)
    }

    // Crear un nuevo objeto
    open fun create(
        obj: T,
        listener: Response.Listener<T>,
        errorListener: Response.ErrorListener
    ) {
        val jsonObject = convertObjectToJson(obj)
        val request = JsonObjectRequest(
            Request.Method.POST, apiUrl, jsonObject,
            { response ->
                val createdObj = parseJsonToObject(response)
                listener.onResponse(createdObj)
            },
            errorListener
        )
        requestQueue.add(request)
    }

    // Actualizar un objeto existente
    open fun update(
        obj: T,
        listener: Response.Listener<T>,
        errorListener: Response.ErrorListener
    ) {
        val jsonObject = convertObjectToJson(obj)
        val request = JsonObjectRequest(
            Request.Method.PUT, apiUrl, jsonObject,
            { response ->
                val updatedObj = parseJsonToObject(response)
                listener.onResponse(updatedObj)
            },
            errorListener
        )
        requestQueue.add(request)
    }

    // Eliminar un objeto por su ID
    open fun delete(
        id: Int,
        listener: Response.Listener<Void>,
        errorListener: Response.ErrorListener
    ) {
        val url = "$apiUrl/$id"
        val request = JsonObjectRequest(
            Request.Method.DELETE, url, null,
            { response ->
                listener.onResponse(null)
            },
            errorListener
        )
        requestQueue.add(request)
    }

    private fun convertObjectToJson(obj: T): JSONObject {
        val jsonString = gson.toJson(obj)
        return JSONObject(jsonString)
    }

    protected fun parseJsonToObject(jsonObject: JSONObject): T {
        return gson.fromJson(jsonObject.toString(), clazz)
    }
}
