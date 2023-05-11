package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.PostService
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.type.AttachmentType

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun playVideo(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var post: Post
    private val urlAuthor = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
    private val urlAttachment = "http://10.0.2.2:9999/attachment/${post.attachment}"

    init {
        binding.like.setOnClickListener{
            onInteractionListener.onLike(post)
        }
        binding.share.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        binding.menu.setOnClickListener {
            PopupMenu (it.context, it).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractionListener.onRemove(post)
                            true
                        }
                        R.id.edit -> {
                            onInteractionListener.onEdit(post)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
        binding.playButton.setOnClickListener {
            onInteractionListener.playVideo(post)
        }

        binding.videoContent.setOnClickListener {
            onInteractionListener.playVideo(post)
        }

        binding.avatar.load(urlAuthor)
    }

    fun bind(post: Post) {
        this.post = post
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes.text = PostService.showValues(post.likes)
            like.isChecked = post.likedByMe
            shares.text = PostService.showValues(post.shares)
            videoContent.isVisible = !post.video.isNullOrBlank()
            playButton.isVisible = !post.video.isNullOrBlank()

            if ((post.attachment != null) && (post.attachment.type == AttachmentType.IMAGE)) {
                Glide.with(binding.attachment)
                    .load(urlAttachment)
                    .placeholder(R.drawable.ic_loading_100dp)
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10_000)
                    .into(binding.attachment)
                attachment.isVisible = true
            } else attachment.isVisible = false
        }
    }

    private fun ImageView.load(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_loading_100dp)
            .error(R.drawable.ic_error_100dp)
            .timeout(10_000)
            .circleCrop()
            .into(this)

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