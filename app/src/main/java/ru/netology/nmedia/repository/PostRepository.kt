package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getData(): List<Post>
    fun like(id: Long, isLiked: Boolean): Post
    fun share(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post
}