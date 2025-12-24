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

    init {
        fetchAccountAndFavorites()
    }

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val moviePager: Flow<PagingData<Movie>> = _searchQuery
        .debounce(400L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            repository.getMoviesPager(query)
        }
        .cachedIn(viewModelScope)

    private val _refreshTrigger = MutableStateFlow(0)

    val favoriteMoviesPager: Flow<PagingData<Movie>> = _uiState
        .map { it.accountId }
        .distinctUntilChanged()
        .flatMapLatest { accountId ->
            if (accountId != null) {
                _refreshTrigger.flatMapLatest {
                    repository.getFavoriteMoviesPager(accountId)
                }
            } else {
                kotlinx.coroutines.flow.flowOf(PagingData.empty())
            }
        }
        .cachedIn(viewModelScope)

    fun onSearch(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query) }
        }
    }

    private fun fetchAccountAndFavorites() {
        viewModelScope.launch {
            try {
                val account = repository.getAccountDetails()
                _uiState.update { it.copy(accountId = account.id) }
                
                // Fetch first page of favorites to populate initial state
                val favorites = repository.getFavoriteMovies(account.id, 1)
                val favoriteIds = favorites.results.map { it.id }.filterNotNull().toSet()
                _uiState.update { it.copy(favourites = favoriteIds) }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun toggleFavorite(movieId: Int) {
        val accountId = _uiState.value.accountId ?: return
        val isCurrentlyFavorite = _uiState.value.favourites.contains(movieId)
        
        viewModelScope.launch {
            try {
                repository.addFavorite(accountId, movieId, !isCurrentlyFavorite)
                _uiState.update { state ->
                    val newFavorites = if (isCurrentlyFavorite) {
                        state.favourites - movieId
                    } else {
                        state.favourites + movieId
                    }
                    state.copy(favourites = newFavorites)
                }
                _refreshTrigger.value += 1
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}