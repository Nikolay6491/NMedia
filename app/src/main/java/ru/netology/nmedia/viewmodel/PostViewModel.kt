package ru.netology.nmedia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorId = 0L,
    authorAvatar = "",
    published = "",
    likedByMe = false,
    sharesByMe = false,
    video = null,
    attachment = null,
    hidden = true,
    ownedByMe = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(application).postDao())
    val edited = MutableLiveData(empty)

    @OptIn(ExperimentalCoroutinesApi::class)
    val data: LiveData<FeedModel> = AppAuth.getInstance().data.flatMapLatest { token ->
        repository.data
            .map { posts ->
                FeedModel(
                    posts = posts.map { it.copy(ownedByMe = it.authorId == token.id) },
                    empty = posts.isEmpty()
                )
            }
    }.asLiveData(Dispatchers.Default)

    private val noPhoto = PhotoModel()
    private val _dataState = MutableLiveData<FeedModelState>(FeedModelState.Idle)
    val dataState: LiveData<FeedModelState>
        get() = _dataState
    private val _currentPost = SingleLiveEvent<Post?>()
    val currentPost: LiveData<Post?>
        get() = _currentPost
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel?>
        get() = _photo
    val newerCount: LiveData<Int> = data.switchMap {
        val newerId = it.posts.firstOrNull()?.id ?: 0L
        repository.getNewer(newerId)
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Default)
    }
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun getPostById(id: Long?) {
        viewModelScope.launch {
            try {
                val result = repository.getById(id)
                _currentPost.value = result
            } catch (e: Exception) {
                _dataState.value = FeedModelState.Error
            }
        }
    }

    fun load() = viewModelScope.launch {
        _dataState.value = FeedModelState.Loading
        try {
            repository.getAll()
            _dataState.value = FeedModelState.Idle
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
        }
    }

    fun refresh() = viewModelScope.launch {
        _dataState.value = FeedModelState.Refresh
        try {
            repository.getAll()
            _dataState.value = FeedModelState.Idle
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
        }
    }

    fun edit(post: Post) = viewModelScope.launch {
        edited.value = post
    }

    fun likesById(id: Long, likedByMe: Boolean) = viewModelScope.launch {
        try {
            repository.likes(id, likedByMe)
            _postCreated.postValue(Unit)
            _dataState.value = FeedModelState.Idle
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
        }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        data.value?.posts.orEmpty()
        try {
            repository.removeById(id)
            _dataState.value = FeedModelState.Idle
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

    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    when (_photo.value) {
                        noPhoto -> repository.save(it)
                        else -> _photo.value?.file?.let { file ->
                            repository.saveWithAttachment(it, MediaUpload(file))
                        }
                    }
                    _dataState.value = FeedModelState.Idle
                } catch (e: Exception) {
                    _dataState.value = FeedModelState.Error
                }
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
            _photo.value = noPhoto
        }
    }

    fun loadVisiblePosts() = viewModelScope.launch {
        try {
            repository.showAll()
        } catch (e: Exception) {
            _dataState.value = FeedModelState.Error
        }
    }

    fun clearPhoto() {
        _photo.value = null
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }
}