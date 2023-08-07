/*package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {

    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private var nextId = 1L
    private var posts = emptyList<Post>()

    private val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let {
            posts = gson.fromJson(it, typeToken)
            nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
        } ?: run {
            posts = listOf(
                Post(
                    nextId++,
                    "Нетология. Университет интернет-профессий будущего",
                    "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    "20 мая в 18:36",
                    favorites = 5,
                    shares = 10,
                    views = 120,
                    favoritesByMe = false,
                    sharesByMe = false,
                    video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
                ),
                Post(
                    nextId++,
                    "Нетология. Университет интернет-профессий будущего",
                    "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    "21 мая в 15:46",
                    favorites = 1,
                    shares = 15,
                    views = 1820,
                    favoritesByMe = false,
                    sharesByMe = false,
                    video = null
                ),
                Post(
                    nextId++,
                    "Нетология. Университет интернет-профессий будущего",
                    "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    "22 мая в 13:37",
                    favorites = 45,
                    shares = 150,
                    views = 16520,
                    favoritesByMe = false,
                    sharesByMe = false,
                    video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
                )
            )
        }
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun favoritesById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else
                if(it.favoritesByMe) it.copy(favoritesByMe = !it.favoritesByMe, favorites =  it.favorites - 1)
                else it.copy(favoritesByMe = !it.favoritesByMe, favorites =  it.favorites + 1)
        }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    favoritesByMe = false,
                    sharesByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts
            sync()
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        sync()
    }

    override fun sharesById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(shares = it.shares + 1)
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    private fun sync() {
        prefs.edit().apply {
            putString(key, gson.toJson(posts))
            apply()
        }
    }
}*/
