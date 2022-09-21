package ru.netology.nmedia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.PostService
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias OnListener = (post: Post) -> Unit

class PostsAdapter(
    val context: Context,
    private val onFavoriteListener: OnListener,
    private val onShareListener: OnListener) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding, onFavoriteListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onFavoriteListener: OnListener,
    private val onShareListener: OnListener
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var post: Post

    init {
        binding.favorite.setOnClickListener{
            onFavoriteListener(post)
        }
        binding.share.setOnClickListener {
            onShareListener(post)
        }
    }

    fun bind(post: Post) {
        this.post = post
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            favorites.text = PostService.showValues(post.favorites)
            favorite.setImageResource(
               if (post.favoritesByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            )
            shares.text = PostService.showValues(post.shares)
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}