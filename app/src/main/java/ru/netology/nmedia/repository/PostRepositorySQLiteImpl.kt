package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun favoritesById(id: Long) {
        dao.favoritesById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                favoritesByMe = !it.favoritesByMe,
                favorites = if (it.favoritesByMe) it.favorites - 1 else it.favorites + 1
            )
        }
        data.value = posts
    }

    override fun sharesById(id: Long) {
        posts = posts.map { post ->
            if (post.id != id) post else post.copy(shares = post.shares + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}