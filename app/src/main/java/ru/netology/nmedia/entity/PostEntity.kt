package ru.netology.nmedia.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Long = 0,
    val shares: Long,
    val views: Long,
    val likesByMe: Boolean,
    val sharesByMe: Boolean,
    val video: String? = null
) {
    fun toDto() = Post(id, author, content, published, likes, shares, views, likesByMe, sharesByMe, video)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.content,
                dto.published,
                dto.likes,
                dto.shares,
                dto.views,
                dto.likesByMe,
                dto.sharesByMe,
                dto.video
            )

    }
}