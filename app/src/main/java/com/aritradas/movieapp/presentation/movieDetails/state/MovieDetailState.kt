package com.aritradas.movieapp.presentation.movieDetails.state

import com.aritradas.movieapp.domain.model.MovieDetail

sealed class MovieDetailState {
    object Loading : MovieDetailState()
    data class Success(
        val movieDetail: MovieDetail,
        val isFavorite: Boolean = false
    ) : MovieDetailState()
    data class Error(val message: String) : MovieDetailState()
}