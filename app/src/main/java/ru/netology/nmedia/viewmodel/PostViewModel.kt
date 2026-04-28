package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

private val empty = Post(
    id = 0,
    author = "Netology",
    published = 0L,
    content = "",
    likes = 0,
    likedByMe = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()

    private val _data = MutableLiveData(FeedModel())

    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)

    val draftPost = MutableLiveData("")

    init {
        load()
    }

    fun load() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllDataAsync(object : PostRepository.PostsCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Throwable) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun setDraftPost(text: String) {
        draftPost.postValue(text)
    }

    fun clearDraftPost() {
        draftPost.postValue("")
    }

    fun like(id: Long, isLiked: Boolean) {
        repository.likeAsync(id, isLiked, object : PostRepository.PostsCallback<Post> {
            override fun onSuccess(post: Post) {
//                val updatedPost = repository.like(id, isLiked)
                val currentPosts = _data.value?.posts ?: emptyList()
                val updatedPosts = currentPosts.map { currentPost ->
                    if (currentPost.id == post.id) post else currentPost
                }
                _data.postValue(
                    FeedModel(
                        posts = updatedPosts,
                        empty = updatedPosts.isEmpty()
                    )
                )
            }

            override fun onError(e: Throwable) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }

    fun share(id: Long) {
        repository.share(id)
    }

    fun removeById(id: Long) {
        repository.removeByIdAsync(id, object : PostRepository.PostsCallback<Unit> {
            override fun onSuccess(result: Unit) {
                load()
            }

            override fun onError(e: Throwable) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save(text: String) {
        edited.value?.let { current ->
            if (current.content != text) {
                repository.saveAsync(
                    current.copy(content = text.trim()),
                    object : PostRepository.PostsCallback<Post> {
                        override fun onSuccess(post: Post) {
                            load()
                            edited.postValue(empty)
                            clearDraftPost()
                        }

                        override fun onError(e: Throwable) {
                            _data.postValue(FeedModel(error = true))
                        }
                    }
                )
            }
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun cancelEditing() {
        edited.value = empty
    }
}