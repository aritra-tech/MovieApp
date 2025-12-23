package com.aritradas.movieapp.presentation.movies.state

import com.aritradas.movieapp.domain.model.Movie

data class MoviesUiState(
    val movies: List<Movie> = emptyList(),
    val favourites: Set<Int> = emptySet(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isError: String? = null
)
