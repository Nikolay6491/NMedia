package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAll()
    suspend fun likes(id: Long, likesByMe: Boolean)
    suspend fun save(post: Post)
    suspend fun sharesById(id: Long)
    suspend fun removeById(id: Long)
    fun getNewer(id: Long): Flow<Int>
    suspend fun showAll()
}