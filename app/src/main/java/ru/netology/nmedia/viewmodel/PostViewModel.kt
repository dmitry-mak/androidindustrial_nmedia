package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl

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

    private val repository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao)

    val data = repository.getData()
    val edited = MutableLiveData(empty)

    val draftPost = MutableLiveData("")
    fun setDraftPost(text: String){
        draftPost.value = text
    }
    fun clearDraftPost(){
        draftPost.value = ""
    }

    fun like(id: Long) {
        repository.like(id)
    }

    fun share(id: Long) {
        repository.share(id)
    }

    fun removeById(id: Long) {
        repository.removeById(id)
    }

    fun save(text: String) {
        edited.value?.let {
            if (it.content != text) {
                repository.save(it.copy(content = text.trim()))
            }
        }
        edited.value = empty
        clearDraftPost()
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun cancelEditing() {
        edited.value = empty
    }
}