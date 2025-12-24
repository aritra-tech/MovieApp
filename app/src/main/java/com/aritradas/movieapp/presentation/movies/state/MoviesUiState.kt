package com.aritradas.movieapp.presentation.movies.state

data class MoviesUiState(
    val favourites: Set<Int> = emptySet(),
    val searchQuery: String = ""
)
