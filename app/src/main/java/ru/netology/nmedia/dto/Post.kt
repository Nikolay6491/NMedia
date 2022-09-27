package ru.netology.nmedia.dto

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val favorites: Long = 0,
    var shares: Long = 0,
    val views: Long = 0,
    val favoritesByMe: Boolean,
    val sharesByMe: Boolean,
    val video: String?
)
