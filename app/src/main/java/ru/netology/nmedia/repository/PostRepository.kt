package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow

import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload

import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.PhotoModel

interface PostRepository {
    val data: Flow<List<Post>>

    suspend fun getAll()
    suspend fun getById(id: Long?): Post?
    suspend fun likes(id: Long, likesByMe: Boolean)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun uploadMedia(upload: MediaUpload): Media
    suspend fun sharesById(id: Long)
    suspend fun removeById(id: Long)
    fun getNewer(id: Long): Flow<Int>
    suspend fun showAll()

}