package com.steven.personallibraryapp.ui.addmovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steven.personallibraryapp.data.Movie
import com.steven.personallibraryapp.viewmodel.MovieViewModel
import com.steven.personallibraryapp.viewmodel.MovieViewModelFactory
import com.steven.personallibraryapp.MainActivity
import com.steven.personallibraryapp.R
import com.steven.personallibraryapp.databinding.FragmentAddMovieBinding
import kotlinx.coroutines.launch

class AddMovieFragment : Fragment() {

    private val viewModel: MovieViewModel by viewModels {
        val repository = (requireActivity() as MainActivity).repository
        MovieViewModelFactory(repository)
    }

    private var _binding: FragmentAddMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = arguments?.getParcelable<Movie>("movie")

        if (movie != null) {
            binding.etTitle.setText(movie.title)
            binding.etDirector.setText(movie.director)
            binding.etYear.setText(movie.year.toString())
            binding.etRating.setText(movie.rating.toString())
            binding.btnCancel.text = "Cancel"
            (activity as MainActivity).supportActionBar?.title = "Edit Film" // Ajustar el título dinámicamente
        } else {
            (activity as MainActivity).supportActionBar?.title = "Add Film"
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val director = binding.etDirector.text.toString()
            val year = binding.etYear.text.toString().toIntOrNull() ?: 0
            val rating = binding.etRating.text.toString().toFloatOrNull() ?: 0.0f

            if (title.isNotEmpty() && director.isNotEmpty() && year > 0) {
                val updatedMovie = movie?.copy(title = title, director = director, year = year, rating = rating)
                    ?: Movie(title = title, director = director, year = year, rating = rating)

                lifecycleScope.launch {
                    if (movie == null) {
                        viewModel.insert(updatedMovie)
                    } else {
                        viewModel.update(updatedMovie)
                    }
                    findNavController().navigateUp()
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
