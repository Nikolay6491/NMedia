package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    suspend fun updateContentById(id: Long, content: String)

    @Query("""
       UPDATE PostEntity SET
        favorites = favorites + CASE WHEN favorites THEN -1 ELSE 1 END,
        favoritesByMe = CASE WHEN favoritesByMe THEN 0 ELSE 1 END
        WHERE id = :id;
    """)
    suspend fun favoritesById(id: Long)

    @Query("""
       UPDATE PostEntity SET
        shares = shares + 1
        WHERE id = :id;
    """)
    suspend fun sharesById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    suspend fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)

    @Query("UPDATE posts SET viewed = 1 WHERE viewed = 0")
    suspend fun markRead()
}