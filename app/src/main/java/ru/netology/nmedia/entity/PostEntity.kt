package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post


@Entity
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String= "",
    val content: String,
    val published: Long,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
) {
    fun toDto() = Post(id, author, authorAvatar, content, published, likes, likedByMe,)

    companion object{
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.authorAvatar,
            post.content,
            post.published,
            post.likedByMe,
            post.likes
        )
    }
}