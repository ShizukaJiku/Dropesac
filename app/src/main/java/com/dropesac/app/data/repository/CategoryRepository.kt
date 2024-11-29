package com.dropesac.app.data.repository

import android.content.Context
import com.dropesac.app.data.model.Category

class CategoryRepository(context: Context) : BaseRepository<Category>(
    context = context,
    apiUrl = "https://api.example.com/categories",
    clazz = Category::class.java
)
