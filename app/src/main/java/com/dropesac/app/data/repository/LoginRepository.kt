package com.dropesac.app.data.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dropesac.app.data.model.User
import com.google.gson.Gson
import org.json.JSONObject

class LoginRepository(
    private val context: Context,
    private val apiUrl: String,
) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val gson = Gson()

    fun login(
        username: String,
        password: String,
        listener: Response.Listener<Boolean>,
        errorListener: Response.ErrorListener
    ) {
        val url = apiUrl
        val jsonObject = convertObjectToJson(User(username, password))

        val request = JsonObjectRequest(
            Request.Method.POST,
            url, jsonObject,
            { response ->
                if (response.getBoolean("success")) {
                    listener.onResponse(true)
                } else {
                    listener.onResponse(false)
                }
            },
            { error ->
                errorListener.onErrorResponse(error)
            }
        )

        requestQueue.add(request)
    }

    private fun convertObjectToJson(obj: User): JSONObject {
        val jsonString = gson.toJson(obj)
        return JSONObject(jsonString)
    }
}