package ru.netology.nmedia.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val postDao: PostDao
) : PostRepository {

    override val data: LiveData<List<Post>> = postDao.getAll().map {
        it.map(PostEntity::toDto)
    }

    override suspend fun getAll() {
        val response = PostsApi.service.getAll()
        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }
        val posts = response.body() ?: throw RuntimeException("body is null")
        postDao.insert(posts.map(PostEntity::fromDto))
    }

    override suspend fun likes(id: Long, favoritesByMe: Boolean) {
        val response = PostsApi.service.like(id)
        if (!response.isSuccessful) {
            throw RuntimeException("api error")
        }
        response.body() ?: throw RuntimeException(response.message())
        postDao.likesById(id)
    }


    override suspend fun sharesById(id: Long) {
        Log.e("PostRepositoryImpl", "Share is not yet implemented")
    }

    override suspend fun removeById(id: Long) {
        val response = PostsApi.service.remove(id)
        if (!response.isSuccessful) throw RuntimeException("api error")
        response.body() ?: throw RuntimeException(response.message())
        postDao.removeById(id)
    }

    override suspend fun save(post: Post) {
        val response = PostsApi.service.save(post)
        if (!response.isSuccessful) throw RuntimeException("api error")
        response.body() ?: throw RuntimeException(response.message())
        postDao.save(PostEntity.fromDto(post))
    }
}