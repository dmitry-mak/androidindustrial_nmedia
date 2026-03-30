package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: Long,
    val likesCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val video: String? = null
)