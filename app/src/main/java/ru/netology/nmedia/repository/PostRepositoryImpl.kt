package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {

    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999/"
        private val jsonType = "application/json".toMediaType()
    }

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let {
                it.body?.string()
            }?.let {
                gson.fromJson(it, typeToken.type)
            } ?: emptyList()
    }

    override fun likes(id: Long, likedByMe: Boolean): Post {
        val isLikes: Boolean = !likedByMe
        val favoritesUrl = "${BASE_URL}api/slow/posts/$id/likes"
        val request: Request = if(isLikes) {
            Request.Builder()
                .post(gson.toJson(id).toRequestBody(jsonType))
                .url(favoritesUrl)
                .build()
        } else {
            Request.Builder()
                .delete(gson.toJson(id).toRequestBody(jsonType))
                .url(favoritesUrl)
                .build()
        }
        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, Post::class.java)
            }
    }

    override fun sharesById(id: Long) {

    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        return client.newCall(request).execute()
            .let {
                it.body?.string()
            }?.let {
                gson.fromJson(it, Post::class.java)
            } ?: error("Empty response body")
    }
}