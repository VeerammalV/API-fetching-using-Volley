package com.example.apiviewmodel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apiviewmodel.databinding.ActivitySecondBinding

class SecondActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize ViewModel using ViewModelProvider and ViewModelFactory
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(application))[ViewModel::class.java]

        // Initialize the PostAdapter with click listeners
        postAdapter = PostAdapter(
            object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    // Handle item click event
                    val clickedPost = postAdapter.getPostAtPosition(position)
                    val intent = Intent(this@SecondActivity, SecondActivity::class.java)
                    startActivity(intent)
                }

                override fun onItemLongPressed(position: Int) {
                    // Handle long press event
                    val longPressedPost = postAdapter.getPostAtPosition(position)
                    println("Long pressed post: ${longPressedPost.title}")
                }
            }
        )

        // Set up RecyclerView
        val posts = viewModel.getPosts().value
        binding.recyclerView1.apply {
            layoutManager = LinearLayoutManager(this@SecondActivity)
            adapter = postAdapter
        }

        // Observe changes in the list of posts and update the adapter accordingly
        viewModel.getPosts().observe(this) { posts ->
            posts?.let {
                postAdapter.setPosts(posts)
            }
        }
    }
}
