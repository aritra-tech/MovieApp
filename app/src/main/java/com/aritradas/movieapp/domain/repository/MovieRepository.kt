package com.aritradas.movieapp.domain.repository

import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.model.MovieDetail
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(page: Int = 1): List<Movie>
    suspend fun getMovieDetails(movieId: Int): MovieDetail
}