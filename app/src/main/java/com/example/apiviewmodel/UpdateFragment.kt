import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apiviewmodel.Post
import com.example.apiviewmodel.R
import com.example.apiviewmodel.ViewModel
import com.example.apiviewmodel.ViewModelFactory
import com.example.apiviewmodel.databinding.FragmentUpdateBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize ViewModel using ViewModelProvider and ViewModelFactory
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity().application))[ViewModel::class.java]
        // Inflate the layout for this fragment using data binding
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve the post to be updated and its position from arguments
        val post = arguments?.getParcelable<Post>("positionToUpdate")
        val position = arguments?.getInt("position")

//        val existingTitle = arguments?.getString("title")
//        val existingBody = arguments?.getString("body")

//        if (positionToUpdate == -1) {
//            if (positionToUpdate == -1) {
//                Log.e("UpdateFragment", "Invalid position to update: $positionToUpdate")
//                return
//            }
//            return
//        }

        // Populate UI with existing post data
        binding.title.setText(post?.title)
        binding.body.setText(post?.body)

        // Set click listener for update button
        binding.ButtonUpdate.setOnClickListener {
            // Retrieve title and body from UI
            val title = binding.title.text.toString()
            val body = binding.body.text.toString()

            // Check if title and body are not empty
            if (title.isNotEmpty() && body.isNotEmpty()) {
                // Create updated post object
                val updatedPost = Post(post!!.userId, post.id,title = title, body = body)
                // Call ViewModel's updatePost method to update the post
                viewModel.updatePost(position!!, updatedPost)
                // Navigate back to previous screen
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                // Show toast if title or body is empty
                if (title.isEmpty() || body.isEmpty()) {
                    Toast.makeText(requireContext(), "Title and body cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        // Static method to create a new instance of UpdateFragment
        fun newInstance(position: Int, positionToUpdate: Post): UpdateFragment {
            val fragment = UpdateFragment()
            // Bundle to pass data to fragment
            val args = Bundle().apply {
                putInt("position", position)
                putParcelable("positionToUpdate", positionToUpdate)
//                title?.let{putString("title", it)}
//                body?.let{putString("body", it)}
            }

            // Set arguments for fragment
            fragment.arguments = args
            return fragment
        }
    }
}
