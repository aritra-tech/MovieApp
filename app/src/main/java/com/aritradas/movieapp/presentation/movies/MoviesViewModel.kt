package com.aritradas.movieapp.presentation.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritradas.movieapp.domain.repository.MovieRepository
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.model.MovieDetail
import com.aritradas.movieapp.presentation.movies.state.MoviesEvent
import com.aritradas.movieapp.presentation.movies.state.MoviesUiState
import com.aritradas.movieapp.util.runIO
import kotlinx.coroutines.flow.Flow
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

    suspend fun loadMovies(page: Int = 1) {
        _uiState.update { it.copy(isLoading = true, isError = null) }
        try {
            val movies = repository.getMovies(page = page)
            _uiState.update {
                it.copy(
                    movies = movies,
                    isLoading = false,
                    isError = null
                )
            }
        } catch (e: Exception) {
            _uiState.update { 
                it.copy(
                    isLoading = false,
                    isError = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun onSearch(query: String) = runIO {
        _uiState.update { it.copy(searchQuery = query) }
    }
}


