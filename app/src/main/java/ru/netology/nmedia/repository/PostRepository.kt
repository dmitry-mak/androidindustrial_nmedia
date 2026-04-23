package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getData(): List<Post>
    fun like(id: Long, isLiked: Boolean): Post
    fun share(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post

    fun getAllDataAsync(callback: GetAllCallback)
    fun likeAsync(id: Long, isLiked: Boolean, callback: LikeCallback)
    fun removeByIdAsync (id: Long,callBack: SimpleCallback)
    fun saveAsync(post: Post, callback: SaveCallback)
    fun shareAsync(id: Long, callBack: SimpleCallback)

    interface GetAllCallback{
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface LikeCallback{
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }

    interface SaveCallback{
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }

    interface SimpleCallback{
        fun onSuccess()
        fun onError(e: Exception)
    }
}