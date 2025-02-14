package com.steven.personallibraryapp.repository

import androidx.lifecycle.LiveData
import com.steven.personallibraryapp.data.Movie
import com.steven.personallibraryapp.data.MovieDao

class MovieRepository(private val movieDao: MovieDao) {
    val allMovies: LiveData<List<Movie>> = movieDao.getAllMovies()

    suspend fun insert(movie: Movie) {
        movieDao.insert(movie)
    }

    suspend fun update(movie: Movie) {
        movieDao.update(movie)
    }

    suspend fun delete(movie: Movie) {
        movieDao.delete(movie)
    }
}