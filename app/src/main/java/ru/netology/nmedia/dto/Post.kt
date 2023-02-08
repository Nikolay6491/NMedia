package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val favorites: Int,
    var shares: Int,
    val views: Int,
    val favoritesByMe: Boolean,
    val sharesByMe: Boolean,
    val video: String?
)
