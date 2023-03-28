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

    override suspend fun getAllAsync() {
        val response = PostsApi.retrofitService.getAll()
        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }
        val posts = response.body() ?: throw RuntimeException("body is null")
        postDao.insert(posts.map(PostEntity::fromDto))
    }

    override suspend fun favoritesAsync(id: Long, favoritesByMe: Boolean) {
        val response = PostsApi.retrofitService.favoritesById(id)
        if (!response.isSuccessful) {
            throw RuntimeException("api error")
        }
        response.body() ?: throw RuntimeException(response.message())
        postDao.favoritesById(id)
    }

    override suspend fun saveAsync(post: Post) {
        val response = PostsApi.retrofitService.save(post)
        if (!response.isSuccessful) throw RuntimeException("api error")
        response.body() ?: throw RuntimeException(response.message())
        postDao.save(PostEntity.fromDto(post))
    }

    override suspend fun sharesByIdAsync(id: Long) {
        Log.e("PostRepositoryImpl", "Share is not yet implemented")
    }

    override suspend fun removeByIdAsync(id: Long) {
        val response = PostsApi.retrofitService.removeById(id)
        if (!response.isSuccessful) throw RuntimeException("api error")
        response.body() ?: throw RuntimeException(response.message())
        postDao.removeById(id)
    }
}
