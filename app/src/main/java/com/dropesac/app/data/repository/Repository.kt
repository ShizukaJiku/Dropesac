package com.dropesac.app.data.repository

import com.android.volley.Response

interface Repository<T> {

    fun getAll(
        listener: Response.Listener<List<T>>,
        errorListener: Response.ErrorListener
    )

    fun getById(
        id: Int,
        listener: Response.Listener<T>,
        errorListener: Response.ErrorListener
    )

    fun insert(
        obj: T,
        listener: Response.Listener<T>,
        errorListener: Response.ErrorListener
    )

    fun update(
        obj: T,
        listener: Response.Listener<T>,
        errorListener: Response.ErrorListener
    )

    fun delete(
        id: Int,
        listener: Response.Listener<Void>,
        errorListener: Response.ErrorListener
    )
}
