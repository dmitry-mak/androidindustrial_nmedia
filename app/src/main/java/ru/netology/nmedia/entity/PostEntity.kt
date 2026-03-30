package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post


@Entity
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: Long,
    val likesCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val video: String? = null
) {
    fun toDto() = Post(id, author, content, published, likesCount, sharesCount, isLiked, video)

    companion object{
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.content,
            post.published,
            post.likesCount,
            post.sharesCount,
            post.isLiked,
            post.video
        )
    }
}