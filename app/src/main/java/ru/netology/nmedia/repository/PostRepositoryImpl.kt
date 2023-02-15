package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao
    ) : PostRepository {
    override fun getAll(): LiveData<List<Post>> {
        val ld = MutableLiveData<List<Post>>()
        val l = mutableListOf<Post>()
        dao.getAll().value?.forEach{
            l.add(it.toDto())
        }
        ld.value = l
        return ld
    }

    override fun favoritesById(id: Long) {
        dao.favoritesById(id)
    }

    override fun sharesById(id: Long) {
        dao.sharesById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }
}