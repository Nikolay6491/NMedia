package ru.netology.nmedia.dto

import ru.netology.nmedia.type.AttachmentType


data class Post(
    val id: Long,
    val author: String,

    val authorId: Long,

    val authorAvatar: String = "",
    val content: String,
    val published: String,
    val likes: Long = 0,
    var shares: Long = 0,
    val views: Long = 0,
    val likedByMe: Boolean,
    val sharesByMe: Boolean,
    val video: String?,

    val hidden: Boolean,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean

)

data class Attachment(
    val url: String,


    val type: AttachmentType
)