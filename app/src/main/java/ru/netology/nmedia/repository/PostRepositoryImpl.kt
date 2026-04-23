package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryImpl() : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(
            30, TimeUnit.SECONDS
        )
        .build()

    private val gson = Gson()


    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999/"
        val postType = object : TypeToken<List<Post>>() {}.type
        val jsonType = "application/json".toMediaType()
    }

    override fun getData(): List<Post> {
        val call = client.newCall(
            Request.Builder()
                .url("${BASE_URL}api/slow/posts")
                .build()
        )

        val response = call.execute()

        val stringResponse = response.body.string()

        return gson.fromJson(stringResponse, postType)
    }

    override fun like(id: Long, isLiked: Boolean): Post {
//        val post = getPostById(id)
        val request = if (isLiked) {
            Request.Builder()
                .delete()
                .url("${BASE_URL}api/slow/posts/${id}/likes")
                .build()
        } else {
            Request.Builder()
                .post("".toRequestBody())
                .url("${BASE_URL}api/slow/posts/${id}/likes")
                .build()
        }
        val response = client.newCall(request).execute()
        val stringResponse = response.body.string()

        if (!response.isSuccessful) {
            throw RuntimeException()
        }
        return gson.fromJson(stringResponse, Post::class.java)
    }


    override fun share(id: Long) {
//        dao.share(id)
//        TODO()
    }

    override fun removeById(id: Long) {
        val request =
            Request.Builder()
                .delete()
                .url("${BASE_URL}api/slow/posts/$id")
                .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Unexpected code ${response.code} ${response.message}")
            }
        }
    }

    override fun save(post: Post): Post {
        val call = client.newCall(
            Request.Builder()
                .post(gson.toJson(post).toRequestBody(jsonType))
                .url("${BASE_URL}api/slow/posts")
                .build()
        )

        val response = call.execute()
        val stringResponse = response.body.string()

        return gson.fromJson(stringResponse, Post::class.java)
    }

    override fun getAllDataAsync(callback: PostRepository.GetAllCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        println(1)
        client.newCall(request)
            .enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    try {
                        println(2)
                        val posts =
                            response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(posts, postType))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
        println(3)
    }

    override fun likeAsync(id: Long, isLiked: Boolean, callback: PostRepository.LikeCallback) {
        val request = if (isLiked) {
            Request.Builder()
                .delete()
                .url("${BASE_URL}api/slow/posts/${id}/likes")
                .build()
        } else {
            Request.Builder()
                .post("".toRequestBody())
                .url("${BASE_URL}api/slow/posts/${id}/likes")
                .build()
        }
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    if (!response.isSuccessful) {
                        throw RuntimeException("Unexpected code ${response.code} ${response.message}")
                    }
                    callback.onSuccess(
                        gson.fromJson(body, Post::class.java)
                    )
                } catch (e: Exception) {
                    callback.onError(e)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun removeByIdAsync(id: Long, callBack: PostRepository.SimpleCallback) {
        val request =
            Request.Builder()
                .delete()
                .url("${BASE_URL}api/slow/posts/$id")
                .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                try {


                    if (!response.isSuccessful) {
                        throw RuntimeException("Unexpected code ${response.code} ${response.message}")
                    }
                    callBack.onSuccess()
                } catch (e: Exception) {
                    callBack.onError(e)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callBack.onError(e)
            }
        })
    }


    override fun saveAsync(post: Post, callback: PostRepository.SaveCallback) {
        val request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}api/slow/posts")
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    if (!response.isSuccessful) {
                        throw RuntimeException("Unexpected code ${response.code} ${response.message}")
                    }
                    callback.onSuccess(gson.fromJson(body, Post::class.java))
                } catch (e: Exception) {
                    callback.onError(e)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

        })
    }

    override fun shareAsync(id: Long, callBack: PostRepository.SimpleCallback) {
        callBack.onSuccess()
    }


    private fun getPostById(id: Long): Post {
        val call = client.newCall(
            Request.Builder()
                .url("${BASE_URL}api/slow/posts/$id")
                .build()
        )
        val response = call.execute()
        val stringResponse = response.body.string()
        return gson.fromJson(stringResponse, Post::class.java)
    }
}

