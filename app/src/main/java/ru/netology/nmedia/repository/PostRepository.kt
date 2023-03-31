package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAllAsync()
    suspend fun favoritesAsync(id: Long, favoritesByMe: Boolean)
    suspend fun saveAsync(post: Post)
    suspend fun sharesByIdAsync(id: Long)
    suspend fun removeByIdAsync(id: Long)
    fun getNewerCount(newerPostId: Long): Flow<Int>
    suspend fun markRead()
}