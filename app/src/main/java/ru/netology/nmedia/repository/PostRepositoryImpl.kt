package ru.netology.nmedia.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
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

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likesAsync(id: Long, likedByMe: Boolean, callback: PostRepository.Callback<Post>) {
        val isLikes: Boolean = !likedByMe
        val likesUrl = "${BASE_URL}api/slow/posts/$id/likes"
        val request: Request = if(isLikes) {
            Request.Builder()
                .post(gson.toJson(id).toRequestBody(jsonType))
                .url(likesUrl)
                .build()
        } else {
            Request.Builder()
                .delete(gson.toJson(id).toRequestBody(jsonType))
                .url(likesUrl)
                .build()
        }
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(gson.fromJson(response.body?.string(), Post::class.java))
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun sharesByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {
        Log.e("PostRepositoryImpl", "Share is not yet implemented")
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(Post(0, "", "", "", 0, 0, 0,
                        likedByMe = true,
                        sharesByMe = true,
                        video = ""
                    ))
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Post>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(post)
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }
}