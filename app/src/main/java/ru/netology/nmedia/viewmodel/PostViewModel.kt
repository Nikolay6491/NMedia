package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    published = "",
    favoritesByMe = false,
    sharesByMe = false,
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    val edited = MutableLiveData(empty)
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts , empty = posts.isEmpty())
            } catch (e: Exception) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun favoriteById(id: Long, favoritesByMe: Boolean) {
        thread {
            val newPost = repository.favorites(id, favoritesByMe)
            val old = _data.value?.posts.orEmpty()
            val posts = listOf(newPost)+old
            _data.postValue(FeedModel(posts=posts))
        }
    }

    fun removeById(id: Long) = repository.removeById(id)

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun save() {
        thread {
            edited.value?.let {
                try {
                    repository.save(it)
                    _postCreated.postValue(Unit)
                } catch (e: Exception) {
                    //TODO
                }
            }
            edited.postValue(empty)
        }
    }
}