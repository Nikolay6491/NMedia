package ru.netology.nmedia.repository

import okhttp3.Callback
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun favoritesAsync(id: Long, favoritesByMe: Boolean, callback: Callback<Post>)
    fun saveAsync(post: Post, callback: Callback<Post>)
    fun sharesByIdAsync(id: Long, callback: Callback<Post>)
    fun removeByIdAsync(id: Long, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(posts: T)
        fun onError(e: Exception)
    }
}