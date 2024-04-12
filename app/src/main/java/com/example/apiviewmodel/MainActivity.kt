package com.example.apiviewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apiviewmodel.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewModel using ViewModelProvider and ViewModelFactory
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(application))[ViewModel::class.java]

        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust padding to accommodate system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Set click listener for floating action button to add a new post
        val fab: FloatingActionButton = binding.floatingActionButton
        fab.setOnClickListener{
            supportFragmentManager.commit {
                replace(R.id.add_fragment, AddFragment.newInstance())
                addToBackStack(null)
            }
        }

        // Initialize PostAdapter with item click and long press listeners
        postAdapter = PostAdapter(object : OnItemClickListener {
            override fun onItemClick(position: Int) {

                // Handle item click event
                val postToUpdate = postAdapter.getPostAtPosition(position)
                val updateFragment = UpdateFragment.newInstance(position, postToUpdate)
                supportFragmentManager.commit {
                    replace(R.id.add_fragment, updateFragment)
                    addToBackStack(null)
                }
            }

            override fun onItemLongPressed(position: Int) {
                // Handle long press event to delete a post
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Delete Post")
                    .setMessage("Are you sure want to delete this post?")
                    .setPositiveButton("Yes") {_, _ ->
                        viewModel.deletePost(position)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        })


        // Set up RecyclerView with PostAdapter
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
        }


        // Move to SecondActivity when a button is clicked
        binding.button.setOnClickListener {
            moveToSecondActivity()
        }

        // Observe changes in the list of posts and update the adapter accordingly
        lifecycleScope.launch {
            viewModel.fetchPosts()?.observe(this@MainActivity) { posts ->
                posts?.let {
                    viewModel.setPosts(posts)
                    postAdapter.setPosts(posts)
                    postAdapter.notifyDataSetChanged()
                }
            }
            viewModel.fetchPosts()
        }

        viewModel.getPosts().observe(this) { posts ->
            posts?.let {
                postAdapter.setPosts(it)
            }
        }

        // Observe updated position and notify adapter about the change
        viewModel.updatedPosition.observe(this, Observer { position ->
            position?.let {
                postAdapter.notifyItemChanged(position)
            }
        })

        // Observe deleted position and remove post from the adapter
        viewModel.deletedPosition.observe(this, Observer { position->
            position?.let {

//                Log.e("activity", position.toString())
//                Log.e("cloned", postAdapter.posts.size.toString())

                postAdapter.removePost(position)
            }
        })

    }

    // Move to SecondActivity
    private fun moveToSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }

}
