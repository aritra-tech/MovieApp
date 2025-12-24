package com.aritradas.movieapp.presentation.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritradas.movieapp.domain.model.MovieDetail
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.presentation.movieDetails.state.MovieDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val state = _state.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _state.value = MovieDetailState.Loading
            try {
                val detail = repository.getMovieDetails(movieId)
                _state.value = MovieDetailState.Success(detail)
            } catch (e: Exception) {
                _state.value = MovieDetailState.Error(e.message ?: "Failed to load movie details")
            }
        }
    }
}
