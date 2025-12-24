package com.aritradas.movieapp.presentation.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.presentation.movies.state.MoviesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    fun loadMovies(page: Int = 1) {
        viewModelScope.launch {
            Log.d("MoviesViewModel", "Loading movies - page: $page")
            _uiState.update { it.copy(isLoading = true, isError = null) }

            try {
                val movies = repository.getMovies(page = page)
                Log.d("MoviesViewModel", "Movies loaded successfully: ${movies.size} items")

                _uiState.update {
                    it.copy(
                        movies = movies,
                        isLoading = false,
                        isError = null
                    )
                }
            } catch (e: Exception) {
                Log.e("MoviesViewModel", "Error loading movies", e)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun onSearch(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query) }
        }
    }
}