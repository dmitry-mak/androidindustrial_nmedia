package ru.netology.nmedia.adapter

import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.appcompat.widget.PopupMenu
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.DiffMethods
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post


interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onOpen(post: Post)
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            publishDay.text = DiffMethods.getCurrentDateFormatted(post.published)
            postContent.text = post.content

            binding.root.setOnClickListener { onInteractionListener.onOpen(post) }
            postContent.setOnClickListener { onInteractionListener.onOpen(post) }

            likeIcon.isChecked = post.isLiked
            likeIcon.text = DiffMethods.convertNumber(post.likesCount)
            likeIcon.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            shareIcon.text = DiffMethods.convertNumber(post.sharesCount)
            shareIcon.setOnClickListener { onInteractionListener.onShare(post) }
            moreButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
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

            val videoUrl = post.video?.trim().orEmpty()
            videoContainer.isVisible = videoUrl.isNotBlank()
            val openVideo: () -> Unit = {
                if (videoUrl.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, videoUrl.toUri())
                    val context = itemView.context
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }
            }
            videoContainer.setOnClickListener { openVideo() }
            videoPreview.setOnClickListener { openVideo() }
            videoPlay.setOnClickListener { openVideo() }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return oldItem == newItem
    }
}



