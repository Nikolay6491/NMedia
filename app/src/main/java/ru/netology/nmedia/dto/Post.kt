package ru.netology.nmedia.dto

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val favorites: Long = 0,
    val shares: Long = 0,
    val removes: Long = 0,
    val favoritesByMe: Boolean
)