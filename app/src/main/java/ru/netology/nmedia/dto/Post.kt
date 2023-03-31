package ru.netology.nmedia.dto

import ru.netology.nmedia.type.AttachmentType


data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String = "",
    val content: String,
    val published: String,
    val favorites: Long = 0,
    var shares: Long = 0,
    val views: Long = 0,
    val favoritesByMe: Boolean,
    val sharesByMe: Boolean,
    val video: String?,
    val show: Boolean,
    val attachment: Boolean? = null
)

data class Attachment(
    val url: String,
    val description: String,
    val type: AttachmentType
)