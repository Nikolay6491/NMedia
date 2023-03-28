package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAllAsync()
    suspend fun favoritesAsync(id: Long, favoritesByMe: Boolean)
    suspend fun saveAsync(post: Post)
    suspend fun sharesByIdAsync(id: Long)
    suspend fun removeByIdAsync(id: Long)
}