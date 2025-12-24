package com.aritradas.movieapp.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.presentation.movies.state.MoviesUiState
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


    val moviePager: Flow<PagingData<Movie>> = repository.getMoviesPager()
        .cachedIn(viewModelScope)

    fun onSearch(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query) }
        }
    }
}