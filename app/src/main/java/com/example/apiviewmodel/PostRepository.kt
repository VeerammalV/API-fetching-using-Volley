package com.example.apiviewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

//Repository used as a mediator between the ViewModel and a data source, in this case, an API endpoint.
class Repository {

    // Companion object to implement Singleton pattern for Repository
    companion object {
        private var instance: Repository? = null

        // Get instance of Repository, creating one if it doesn't exist
        fun getInstance() = instance ?:
        synchronized(Repository::class.java) {
                instance ?: Repository().also {
                    instance = it
                }
        }
    }

    // Function to fetch posts from the API endpoint
    fun fetchPosts(context: Context): MutableLiveData<ArrayList<Post>> {

        // API endpoint URL
        val apiUrl = "https://jsonplaceholder.typicode.com/posts"

        // Initialize Volley request queue
        val requestQueue = Volley.newRequestQueue(context)

        // MutableLiveData to hold the list of posts
        val postsLiveData = MutableLiveData<ArrayList<Post>>()

            // Create a JSON array request to fetch data from the API
            val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, apiUrl, null, { response ->
                // Handle successful response
                val posts = ArrayList<Post>()
                // Iterate through the JSON array and create Post objects
                for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val post = Post(
                            jsonObject.getInt("userId"),
                            jsonObject.getInt("id"),
                            jsonObject.getString("title"),
                            jsonObject.getString("body")
                        )
                        posts.add(post)
                    }
                // Post the fetched list of posts to MutableLiveData
                postsLiveData.postValue(posts)
                },
                { error ->
                    // Handle error
                    error.printStackTrace()
                })

            requestQueue.add(jsonArrayRequest)

        // Return MutableLiveData holding the list of posts
        return postsLiveData
    }

}

