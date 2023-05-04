package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Long = 0,
    var shares: Long = 0,
    val views: Long = 0,
    val likedByMe: Boolean,
    val sharesByMe: Boolean,
    val video: String?
)
