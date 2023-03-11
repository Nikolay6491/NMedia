/* package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFilesImpl(val context: Context) : PostRepository {

    private val gson = Gson()
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }

    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, typeToken)
                nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
            }
        } else {
            posts = listOf(
                Post(
                    nextId++,
                    "Нетология. Университет интернет-профессий будущего",
                    "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    "20 мая в 18:36",
                    favoritesByMe = false,
                    sharesByMe = false,
                    video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
                ),
                Post(
                    nextId++,
                    "Нетология. Университет интернет-профессий будущего",
                    "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    "21 мая в 15:46",
                    favoritesByMe = false,
                    sharesByMe = false,
                    video = null
                ),
                Post(
                    nextId++,
                    "Нетология. Университет интернет-профессий будущего",
                    "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    "22 мая в 13:37",
                    favoritesByMe = false,
                    sharesByMe = false,
                    video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
                )
            )
        }
        data.value = posts
    }

    override fun getAll(): List<Post> = data
    
    override fun favoritesById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else
                if(it.favoritesByMe) it.copy(favoritesByMe = !it.favoritesByMe, favorites =  it.favorites - 1)
                else it.copy(favoritesByMe = !it.favoritesByMe, favorites =  it.favorites + 1)
        }
        data.value = posts
    }

    override fun save(post: Post): Post {
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
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    override fun sharesById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}*/
