package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun share(id: Long)

    fun getAllDataAsync(callback: PostsCallback<List<Post>>)
    fun likeAsync(id: Long, isLiked: Boolean, callback: PostsCallback<Post>)
    fun removeByIdAsync(id: Long, callBack: PostsCallback<Unit>)
    fun saveAsync(post: Post, callback: PostsCallback<Post>)
    fun shareAsync(id: Long, callBack: PostsCallback<Unit>)


    interface PostsCallback <T>{
        fun onSuccess(result: T)
        fun onError(error: Throwable)
    }
}