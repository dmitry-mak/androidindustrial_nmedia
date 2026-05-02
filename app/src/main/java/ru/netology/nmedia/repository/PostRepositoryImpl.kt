package ru.netology.nmedia.repository

import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl() : PostRepository {

    override fun getAllDataAsync(callback: PostRepository.PostCallback<List<Post>>) {
        PostApi.service.getAllData()
            .enqueue(object : retrofit2.Callback<List<Post>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Post>>,
                    response: retrofit2.Response<List<Post>>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(Exception("error"))
                        return
                    }
                    val body = response.body()
                    if (body == null) {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception("error"))
                }
            })
    }

    override fun likeAsync(
        id: Long,
        isLiked: Boolean,
        callback: PostRepository.PostCallback<Post>
    ) {
        val call = if (isLiked) {
            PostApi.service.unlikeById(id)
        } else {
            PostApi.service.likeById(id)
        }
        call.enqueue(object : retrofit2.Callback<Post> {
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(Exception("error"))
                    return
                }
                val body = response.body()
                if (body == null) {
                    callback.onError(Exception("body is null"))
                    return
                }
                callback.onSuccess(body)
            }

            override fun onFailure(
                call: retrofit2.Call<Post>,
                t: Throwable
            ) {
                callback.onError(Exception("error"))
            }

        })
    }


    override fun removeByIdAsync(
        id: Long,
        callBack: PostRepository.PostCallback<Unit>
    ) {
        PostApi.service.deleteById(id)
            .enqueue(object : retrofit2.Callback<Unit> {
                override fun onResponse(
                    call: retrofit2.Call<Unit>,
                    response: retrofit2.Response<Unit>
                ) {
                    if (!response.isSuccessful) {
                        callBack.onError(Exception("error"))
                        return
                    }
                    callBack.onSuccess(Unit)
                }

                override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                    callBack.onError(Exception("error"))
                }
            })
    }

    override fun saveAsync(post: Post, callback: PostRepository.PostCallback<Post>) {
        PostApi.service.save(post)
            .enqueue(object : retrofit2.Callback<Post> {
                override fun onResponse(
                    call: retrofit2.Call<Post>,
                    response: retrofit2.Response<Post>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(Exception("error"))
                        return
                    }
                    val body = response.body()
                    if (body == null) {
                        callback.onError(Exception("body is null"))
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(
                    call: retrofit2.Call<Post>,
                    t: Throwable
                ) {
                    callback.onError(Exception("error"))
                }
            })
    }


    override fun share(id: Long) {
    }
}

