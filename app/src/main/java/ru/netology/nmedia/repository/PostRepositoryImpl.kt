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
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.api.PostApiService
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


//    override fun getAllData(): List<Post>{
//        return PostApi.service.getAllData()
//            .execute()
//            .body()
//            .orEmpty()
//    }

    override fun getAllDataAsync(callback: PostRepository.PostCallback<List<Post>>) {
        PostApi.service.getAllData()
            .enqueue(object : retrofit2.Callback<List<Post>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Post>>,
                    response: retrofit2.Response<List<Post>>
                ){
                    if (!response.isSuccessful) {
                        callback.onError(Exception("error"))
                        return
                    }
                    val body = response.body()
                    if (body == null) {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(body )
                }

                override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable){
                    callback.onError(Exception("error"))
                }

//                override fun onResponse(call: Call, response: Response) {
//                    try {
//                        val posts =
//                            response.body?.string() ?: throw RuntimeException("body is null")
//                        callback.onSuccess(gson.fromJson(posts, postType))
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }

//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
            })
    }

    override fun likeAsync(
        id: Long,
        isLiked: Boolean,
        callback: PostRepository.PostCallback<Post>
    ) {
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
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                try {
                    val body = response.body.string() ?: throw RuntimeException("body is null")
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

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun removeByIdAsync(id: Long, callBack: PostRepository.PostCallback<Unit>) {
        val request =
            Request.Builder()
                .delete()
                .url("${BASE_URL}api/slow/posts/$id")
                .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                try {


                    if (!response.isSuccessful) {
                        throw RuntimeException("Unexpected code ${response.code} ${response.message}")
                    }
                    callBack.onSuccess(Unit)
                } catch (e: Exception) {
                    callBack.onError(e)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callBack.onError(e)
            }
        })
    }


    override fun saveAsync(post: Post, callback: PostRepository.PostCallback<Post>) {
        val request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}api/slow/posts")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                try {
                    val body = response.body.string()
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

//    override fun shareAsync(id: Long, callBack: PostRepository.PostCallback<Unit>) {
//        callBack.onSuccess(Unit)
//    }

    override fun share(id: Long) {
//        dao.share(id)
//        TODO()
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

