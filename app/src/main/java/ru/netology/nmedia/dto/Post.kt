package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val favorites: Int = 0,
    var shares: Int = 0,
    val views: Int = 0,
    val favoritesByMe: Boolean,
    val sharesByMe: Boolean,
    val video: String?
)
