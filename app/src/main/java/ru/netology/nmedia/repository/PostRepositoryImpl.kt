package ru.netology.nmedia.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.AppError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.type.AttachmentType
import java.io.IOException

class PostRepositoryImpl(
    private val postDao: PostDao
) : PostRepository {

    override var data = postDao.getAllVisible()
        .map(List<PostEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val posts = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(posts.map(PostEntity::fromDto))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getById(id: Long?): Post? {
        return if(id!=null) postDao.getById(id).toDto() else null
    }

    override suspend fun likes(id: Long, likesByMe: Boolean) {
        try {
            val response = PostsApi.service.like(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            val posts = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(posts))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun sharesById(id: Long) {
        Log.e("PostRepositoryImpl", "Share is not yet implemented")
    }

    override suspend fun removeById(id: Long) {
        try {
            val response = PostsApi.service.remove(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.service.save(post)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            response.body() ?: throw ApiError(response.code(), response.message())
            postDao.save(PostEntity.fromDto(post))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = uploadMedia(upload)
            val postWithAttachment =
                post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: AppError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun uploadMedia(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )

            val response = PostsApi.service.uploadMedia(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getNewer(id: Long): Flow<Int> = flow {
        while (true) {
            delay(10_000L)
            val response = PostsApi.service.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(body.toEntity().map {
                it.copy(hidden = true)
            })

            emit(body.size)
        }
    }
        .catch { e -> throw AppError.from(e) }
        .flowOn(Dispatchers.Default)

    override suspend fun showAll() {
        postDao.showAll()
    }
}