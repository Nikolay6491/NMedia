package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import androidx.lifecycle.viewModelScope as viewModelScope


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    published = "",
    favoritesByMe = false,
    sharesByMe = false,
    video = null,
    show = false,
    attachment = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(application).postDao())
    val edited = MutableLiveData(empty)
    val data: LiveData<FeedModel> = repository.data.map (::FeedModel)
        .asLiveData(Dispatchers.Default)

    val newerCount: LiveData<Int> = data.switchMap {
        val newerId = it.posts.firstOrNull()?.id ?: 0L
        repository.getNewerCount(newerId)
            .asLiveData()
    }

    private val _dataState = MutableLiveData<FeedModelState>(FeedModelState.Idle)
    val dataState: LiveData<FeedModelState>
        get() = _dataState
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        _dataState.value = FeedModelState.Loading
        try {
            repository.getAllAsync()
            _dataState.value = FeedModelState.Idle
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
        }
    }

    fun edit(post: Post) = viewModelScope.launch {
        edited.value = post
    }

    fun favoriteById(id: Long, favoritesByMe: Boolean) = viewModelScope.launch {
        try {
            repository.favoritesAsync(id, favoritesByMe)
            _postCreated.postValue(Unit)
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
        }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        data.value?.posts.orEmpty()
        try {
            repository.removeByIdAsync(id)
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
        }
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun save() = viewModelScope.launch {
        edited.value?.let {
            repository.saveAsync(it)
        }
        edited.postValue(empty)
    }

    fun markRead() {
        viewModelScope.launch {
            repository.markRead()
        }
    }
}
