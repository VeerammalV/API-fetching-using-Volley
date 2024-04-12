package com.example.apiviewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Array

//ViewModel is an AndroidViewModel responsible for managing data related to the UI, specifically handling data related to posts fetched from an API

// ViewModel class extends AndroidViewModel
class ViewModel(application: Application): AndroidViewModel(application) {

    // Repository instance
    private var repository: Repository? = null

    // MutableLiveData to hold the list of posts
    private val postsLiveData = MutableLiveData<ArrayList<Post>>().apply{
       value =  ArrayList()      // Initialize as an empty ArrayList
    }

    // MutableLiveData to hold the position of an updated post
    val updatedPosition: MutableLiveData<Int?> by lazy {
        MutableLiveData<Int?>().apply { value = 0 }
    }

    // MutableLiveData to hold the position of a deleted post
    val deletedPosition: MutableLiveData<Int?> by lazy {
        MutableLiveData<Int?>().apply { value = 0 }
    }

    // Function to fetch posts from the repository asynchronously
    suspend fun fetchPosts(): MutableLiveData<ArrayList<Post>>? =
        withContext(Dispatchers.IO) { return@withContext repository?.fetchPosts(getApplication<Application>().applicationContext)
        }

    // Function to set the list of posts
    fun setPosts(list: ArrayList<Post>) {
        postsLiveData.postValue(list as ArrayList<Post>?)
    }

    // Function to get the MutableLiveData holding the list of posts
    fun getPosts(): MutableLiveData<ArrayList<Post>> {
        return postsLiveData
    }

    // Function to add a new post
    fun addPost(post: Post) {
        val cloned = ArrayList(postsLiveData.value ?: arrayListOf())
        cloned.add(post)
        postsLiveData.postValue(cloned)
    }

    // Function to delete a post by position
    fun deletePost(position: Int) {
        val cloned = ArrayList(postsLiveData.value?: arrayListOf())

        Log.e("model", position.toString())
        Log.e("cloned", cloned.size.toString())
        if (position in 0 until cloned.size){
            cloned.removeAt(position)
            postsLiveData.postValue(cloned)
            deletedPosition.postValue(position)

        }
    }

    //ALTERNATIVE METHOD FOR DELETE-POST
    //    fun deletePost(position: Int) {
    //        val cloned = ArrayList(postsLiveData.value ?: arrayListOf())
    //        if (position >= 0 && position < cloned.size) {
    //            cloned.removeAt(position)
    //            postsLiveData.postValue(cloned)
    //        }
    //    }


    // Function to update a post by position
    fun updatePost(position: Int, updatedPost: Post) {
        val cloned = ArrayList(postsLiveData.value?: arrayListOf())
        cloned[position] = updatedPost
        postsLiveData.postValue(cloned)
        updatedPosition.postValue(position)
    }

    // Initialize the repository in the constructor
    init {
        repository = Repository.getInstance()
    }

    // Companion object to implement Singleton pattern
    companion object {
        private var instance: ViewModel? = null
        // Function to get an instance of ViewModel
        fun getInstance(application: Application) = instance?:
        synchronized(ViewModel::class.java) {
            instance?: ViewModel(application).also {
                instance = it
            }
        }
    }
}
