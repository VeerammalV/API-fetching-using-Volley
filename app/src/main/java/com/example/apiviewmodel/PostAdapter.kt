package com.example.apiviewmodel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apiviewmodel.databinding.ItemPostBinding

// Interface to handle item click and long press events
interface OnItemClickListener {
    fun onItemClick(position: Int)
    fun onItemLongPressed(position: Int)
}

// RecyclerView adapter for displaying posts
class PostAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // List of posts
    private var posts: ArrayList<Post> = arrayListOf()

    // ViewHolder for each post item
    class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Function to bind post data to the ViewHolder
        fun bind(post: Post) {
            binding.title.text = post.title
            binding.body.text = post.body
        }
    }

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    // Return number of items in the list
    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)

        // Set click listener for item
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        // Set long click listener for item
        holder.itemView.setOnLongClickListener {
            listener.onItemLongPressed(holder.adapterPosition)
            true   // Consume the long click event
        }
    }

    // Set posts to display
    @SuppressLint("NotifyDataSetChanged")
    fun setPosts(posts: List<Post>) {
        this.posts = posts as ArrayList<Post>
    }

    // Get post at a given position
    fun getPostAtPosition(position: Int): Post {
        return posts[position]
    }

    // Remove post at a given position
    fun removePost(position: Int) {
        if (posts.isNotEmpty() && position < posts.size) {
            posts.removeAt(position)
            notifyItemRemoved(position)   // Notify adapter about item removal
        }
    }


}

