package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Attachment

data class PostModel(
    val id: Long = 0L,
    val author: String = "",
    val content: String = "",
    val published: String = "",
    var likedByMe: Boolean = false,
    val likes: Int = 0,
    val authorAvatar: String = "",
    val isHidden: Boolean = false,
    val attachment: Attachment? = null
)