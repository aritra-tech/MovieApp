package com.aritradas.movieapp.presentation.movies.state

sealed interface MoviesEvent {
    data class OnSearchChanged(val query: String) : MoviesEvent
    data class OnToggleFavourite(val movieId: Int) : MoviesEvent
}