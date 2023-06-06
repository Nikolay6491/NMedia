package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun likes(id: Long, favoritesByMe: Boolean)
    suspend fun save(post: Post)
    suspend fun sharesById(id: Long)
    suspend fun removeById(id: Long)
}