package com.steven.personallibraryapp.ui.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steven.personallibraryapp.MainActivity
import com.steven.personallibraryapp.adapter.MovieAdapter
import com.steven.personallibraryapp.viewmodel.MovieViewModel
import com.steven.personallibraryapp.viewmodel.MovieViewModelFactory
import com.steven.personallibraryapp.R
import com.steven.personallibraryapp.databinding.FragmentMovieListBinding

class MovieListFragment : Fragment() {

    private val viewModel: MovieViewModel by viewModels {
        val repository = (requireActivity() as MainActivity).repository
        MovieViewModelFactory(repository)
    }

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerViewMovies
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = MovieAdapter { movie ->
            val bundle = Bundle().apply {
                putParcelable("movie", movie)
            }
            findNavController().navigate(R.id.action_movieList_to_showMovie, bundle)
        }

        recyclerView.adapter = adapter

        viewModel.allMovies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
        }

        binding.btnAddMovie.setOnClickListener {
            findNavController().navigate(R.id.action_movieList_to_addMovie)
        }

        binding.btnCreatorInfo.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Creator Info")
            builder.setMessage("This app was created by TheDarkKnight. Contact: batman@iesebre.com")
            builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
