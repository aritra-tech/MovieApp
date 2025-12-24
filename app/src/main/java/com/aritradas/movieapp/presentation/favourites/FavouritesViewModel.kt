package com.aritradas.movieapp.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import com.aritradas.movieapp.presentation.movies.state.MoviesUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritesViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(0)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val favoriteMoviesPager: Flow<PagingData<Movie>> = combine(
        _uiState.map { it.accountId }.distinctUntilChanged(),
        _searchQuery,
        _refreshTrigger
    ) { accountId, query, _ ->
        accountId to query
    }.flatMapLatest { (accountId, query) ->
        if (accountId != null) {
            favoriteRepository.getFavoriteMoviesPager(accountId)
                .map { pagingData ->
                    if (query.isBlank()) pagingData
                    else pagingData.filter { movie ->
                        movie.title?.contains(query, ignoreCase = true) == true
                    }
                }
        } else {
            flowOf(PagingData.empty())
        }
    }.cachedIn(viewModelScope)

    init {
        fetchAccountAndFavorites()
    }

    private fun fetchAccountAndFavorites() {
        viewModelScope.launch {
            try {
                val account = favoriteRepository.getAccountDetails()
                _uiState.update { it.copy(accountId = account.id) }
                
                val favorites = favoriteRepository.getFavoriteMovies(account.id, 1)
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
                favoriteRepository.addFavorite(accountId, movieId, !isCurrentlyFavorite)
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

    fun onSearch(query: String) {
        _searchQuery.value = query
    }
}
