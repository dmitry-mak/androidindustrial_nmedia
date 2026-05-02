package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getAllDataAsync(callback: PostCallback<List<Post>>)

    fun likeAsync(
        id: Long,
        isLiked: Boolean,
        callback: PostCallback<Post>
    )
    fun removeByIdAsync(
        id: Long,
        callBack: PostCallback<Unit>
    )
    fun saveAsync(
        post: Post,
        callback: PostCallback<Post>
    )
    fun share(id: Long)
//    fun getAllData(): List<Post>
//    fun like(id: Long, isLiked: Boolean)
//    fun removeById(id: Long)
//    fun save(post: Post)

//    fun getAllDataAsync(callback: GetAllCallBack)

    //    fun likeAsync(id: Long, isLiked: Boolean, callback: PostsCallback<Post>)
//    fun removeByIdAsync(id: Long, callBack: PostsCallback<Unit>)
//    fun saveAsync(post: Post, callback: PostsCallback<Post>)
//    fun shareAsync(id: Long, callBack: PostsCallback<Unit>)


//    interface PostsCallback<T> {
//        fun onSuccess(result: T)
//        fun onError(error: Throwable)
//    }

//    interface GetAllCallBack {
//        fun onSuccess(posts: List<Post>) {}
//        fun onError(error: Exception) {}
//    }

    interface PostCallback<T> {
        fun onSuccess(result: T)
        fun onError(error: Throwable)
    }
}