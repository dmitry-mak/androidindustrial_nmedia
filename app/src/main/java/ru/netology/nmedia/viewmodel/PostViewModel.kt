package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    author = "Netology",
    published = 0L,
    content = "",
    likesCount = 0,
    sharesCount = 0,
    isLiked = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()

    private val _data = MutableLiveData(FeedModel())

    //    val data = repository.getData()
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)

    val draftPost = MutableLiveData("")

    init {
        load()
    }

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))

            _data.postValue(
                try {
                    val posts: List<Post> = repository.getData()
                    FeedModel(posts = posts, empty = posts.isEmpty())
                } catch (e: IOException) {
                    FeedModel(error = true)
                }
            )
        }
    }

    fun setDraftPost(text: String) {
        draftPost.postValue(text)
    }

    fun clearDraftPost() {
        draftPost.postValue("")
    }

    fun like(id: Long, isLiked: Boolean) {
//        repository.like(id)
//        thread {
//            repository.like(id)
//            load()
//        }
        thread {
            val updatedPost = repository.like(id, isLiked)
            val currentPosts = _data.value?.posts ?: emptyList()
            val updatedPosts = currentPosts.map { post ->
                if (post.id == updatedPost.id) updatedPost else post
            }
            _data.postValue(
                FeedModel(
                    posts = updatedPosts,
                    empty = updatedPosts.isEmpty()
                )
            )
        }
    }

    fun share(id: Long) {
        repository.share(id)
    }

    fun removeById(id: Long) {
        thread {
            repository.removeById(id)
            load()
        }
    }

    fun save(text: String) {
        thread {
            edited.value?.let {
                if (it.content != text) {
                    repository.save(it.copy(content = text.trim()))
                    load()
                }
            }
            edited.postValue(empty)
            clearDraftPost()
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun cancelEditing() {
        edited.value = empty
    }
}