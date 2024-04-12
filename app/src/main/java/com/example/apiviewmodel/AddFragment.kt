import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apiviewmodel.Post
import com.example.apiviewmodel.R
import com.example.apiviewmodel.ViewModel
import com.example.apiviewmodel.databinding.FragmentAddBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Fragment responsible for adding new posts
class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: ViewModel

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Initialize UI elements and set click listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make floating action button visible
        activity?.findViewById<FloatingActionButton>(R.id.floatingActionButton)?.visibility = View.VISIBLE

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        // Handle add button click
        binding.ButtonAdd.setOnClickListener {

            // Get title and body text from EditText fields
            val title = binding.title.text.toString()
            val body = binding.body.text.toString()

            // Check if title and body are not empty
            if (title.isNotEmpty() && body.isNotEmpty()) {
                val newPost = Post(-1,-1,title, body)
                // Add the new post to ViewModel
                viewModel.addPost(newPost)
                // Navigate back to the previous fragment
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                // Display a toast message if title or body is empty
                if (title.isEmpty() || body.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }
                // You may want to remove this branch as it may cause confusion
                else {
                    Toast.makeText(requireContext(), "Post Added", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    companion object {
        // Static method to create a new instance of this fragment
        fun newInstance() = AddFragment()
    }
}
