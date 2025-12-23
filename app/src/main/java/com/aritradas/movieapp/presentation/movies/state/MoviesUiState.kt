package com.aritradas.movieapp.presentation.movies.state

data class MoviesUiState(
    val isLoading: Boolean = false,
    val favourites: Set<Int> = emptySet(),
    val searchQuery: String = "",
    val error: String? = null
)
