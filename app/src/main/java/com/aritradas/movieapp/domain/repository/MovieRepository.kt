package com.aritradas.movieapp.domain.repository

import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.model.MovieDetail
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    val favourites: Flow<Set<Int>>
    fun getPagedMovies(query: String?): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): MovieDetail
    suspend fun toggleFavourite(movieId: Int)
}