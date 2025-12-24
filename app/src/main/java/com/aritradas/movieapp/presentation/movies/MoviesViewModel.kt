package com.aritradas.movieapp.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.presentation.movies.state.MoviesUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MoviesViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val moviePager: Flow<PagingData<Movie>> = _searchQuery
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            repository.getMoviesPager(query)
        }
        .cachedIn(viewModelScope)

    fun onSearch(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query) }
        }
    }
}