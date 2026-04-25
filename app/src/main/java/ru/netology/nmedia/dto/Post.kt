package ru.netology.nmedia.dto

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String? = null,
    val content: String,
    val published: Long,
    @SerializedName("likes")
    val likesCount: Int = 0,
    val sharesCount: Int = 0,
    @SerializedName("likedByMe")
    val isLiked: Boolean = false,
    val video: String? = null
)