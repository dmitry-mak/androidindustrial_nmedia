package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryImpl() : PostRepository {

//    override fun getData(): LiveData<List<Post>> = dao.getAll().map { list ->
//        list.map { it.toDto() }
//    }

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

    override fun like(id: Long) {
val post = getPostById(id)
        val request = if(post.isLiked) {
            Request.Builder()
                .delete()
                .url("${BASE_URL}api/slow/posts/${id}/likes")
                .build()
        }else{
            Request.Builder()
                .post("".toRequestBody())
                .url("${BASE_URL}api/slow/posts/${id}/likes")
                .build()
        }
        client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) {
            throw RuntimeException("Unexpected code ${response.code} ${response.message}")
            }
        }

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
            if(!response.isSuccessful) {
            throw RuntimeException("Unexpected code ${response.code} ${response.message}")
            }
        }
    }

    override fun save(post: Post): Post {
        /*
        //        dao.save(PostEntity.fromDto(post))
                val toSave = if (post.id == 0L) {
                    post.copy(published = System.currentTimeMillis())
                } else {
                    post
                }
                dao.save(PostEntity.fromDto(toSave))

         */
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

