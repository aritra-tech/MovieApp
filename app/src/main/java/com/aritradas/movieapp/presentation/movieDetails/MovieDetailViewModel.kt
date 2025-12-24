package com.aritradas.movieapp.presentation.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.presentation.movieDetails.state.MovieDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val state = _state.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _state.value = MovieDetailState.Loading
            try {
                val detail = movieRepository.getMovieDetails(movieId)
                val accountStates = favoriteRepository.getMovieAccountStates(movieId)
                _state.value = MovieDetailState.Success(
                    movieDetail = detail,
                    isFavorite = accountStates.favorite
                )
            } catch (e: Exception) {
                _state.value = MovieDetailState.Error(e.message ?: "Failed to load movie details")
            }
        }
    }

    fun toggleFavorite(movieId: Int) {
        val currentState = _state.value as? MovieDetailState.Success ?: return
        val newFavoriteStatus = !currentState.isFavorite
        
        viewModelScope.launch {
            try {
                val account = favoriteRepository.getAccountDetails()
                favoriteRepository.addFavorite(account.id, movieId, newFavoriteStatus)
                _state.value = currentState.copy(isFavorite = newFavoriteStatus)
            } catch (e: Exception) {
                // Optionally handle error
            }
        }
    }
}
