package com.steven.personallibraryapp.ui.showmovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.steven.personallibraryapp.data.Movie
import com.steven.personallibraryapp.viewmodel.MovieViewModel
import com.steven.personallibraryapp.viewmodel.MovieViewModelFactory
import com.steven.personallibraryapp.MainActivity
import com.steven.personallibraryapp.R
import com.steven.personallibraryapp.databinding.FragmentShowMovieBinding

class ShowMovieFragment : Fragment() {

    private val viewModel: MovieViewModel by viewModels {
        val repository = (requireActivity() as MainActivity).repository
        MovieViewModelFactory(repository)
    }

    private var _binding: FragmentShowMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtenemos el objeto Movie del Bundle
        val movie: Movie? = arguments?.getParcelable("movie")

        movie?.let { movie ->
            binding.tvTitle.text = movie.title
            binding.tvDirector.text = "Director: ${movie.director}"
            binding.tvYear.text = "Year: ${movie.year}"
            binding.tvRating.text = "Rating: ${movie.rating}"

            binding.btnEditMovie.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("movie", movie)
                }
                findNavController().navigate(R.id.action_showMovie_to_addMovie, bundle)
            }

            binding.btnDeleteMovie.setOnClickListener {
                showDeleteConfirmationDialog(movie)
            }
        }
    }

    private fun showDeleteConfirmationDialog(movie: Movie) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this movie from your library?")
            .setPositiveButton("Yes, delete") { dialog, _ ->
                viewModel.delete(movie)
                Toast.makeText(requireContext(), "Movie deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
