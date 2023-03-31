package ru.netology.nmedia.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val favorites: Long = 0,
    val shares: Long,
    val views: Long,
    val favoritesByMe: Boolean,
    val sharesByMe: Boolean,
    val video: String? = null,
    val show: Boolean = false
) {
    fun toDto() = Post(id, author, authorAvatar, content, published, favorites, shares, views, favoritesByMe, sharesByMe, video, show)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.authorAvatar, dto.content, dto.published, dto.favorites, dto.shares, dto.views, dto.favoritesByMe, dto.sharesByMe, dto.video, dto.show)
    }
}