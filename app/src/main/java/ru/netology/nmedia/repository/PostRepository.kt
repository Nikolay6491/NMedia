package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun favoritesById(id: Long)
    fun save(post: Post)
    fun sharesById(id: Long)
    fun removeById(id: Long)
}