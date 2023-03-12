package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun favorites(id: Long, favoritesByMe: Boolean): Post
    fun save(post: Post): Post
    fun sharesById(id: Long)
    fun removeById(id: Long)
}