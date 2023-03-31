package ru.netology.nmedia.repository

import android.util.Log
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException

class PostRepositoryImpl(
    private val postDao: PostDao
) : PostRepository {

    override val data: Flow<List<Post>> = postDao.getAll().map {
        it.map(PostEntity::toDto)
    }
        .flowOn(Dispatchers.Default)

    override suspend fun getAllAsync() {
        try {
            val response = PostsApi.retrofitService.getAll()
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val posts = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(posts.map(PostEntity::fromDto))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun favoritesAsync(id: Long, favoritesByMe: Boolean) {
        try {
            val response = PostsApi.retrofitService.favoritesById(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val posts = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(posts))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveAsync(post: Post) {
        try {
            val response = PostsApi.retrofitService.save(post)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.save(PostEntity.fromDto(post))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun sharesByIdAsync(id: Long) {
        Log.e("PostRepositoryImpl", "Share is not yet implemented")
    }

    override suspend fun removeByIdAsync(id: Long) {
        try {
            val response = PostsApi.retrofitService.removeById(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getNewerCount(newerPostId: Long): Flow<Int> = flow {
        while (true) {
            try {
                delay(10_000)
                val response = PostsApi.retrofitService.getNewer(newerPostId)
                if (!response.isSuccessful) throw ApiError(response.code(), response.message())
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                postDao.insert(body.map(PostEntity::fromDto))
                emit(body.size)
            } catch (e: CancellationException) {
                throw e
            } catch (e: ApiException) {
                throw e
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                throw UnknownError
            }
        }
    }
        .flowOn(Dispatchers.Default)

    override suspend fun markRead() {
        postDao.markRead()
    }
}
