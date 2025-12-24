package com.aritradas.movieapp.domain.repository

import androidx.paging.PagingData
import com.aritradas.movieapp.domain.model.*
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun getAccountDetails(): AccountDetails
    suspend fun addFavorite(accountId: Int, mediaId: Int, isFavorite: Boolean): FavoriteResponse
    suspend fun getFavoriteMovies(accountId: Int, page: Int): DiscoverMoviesResponse
    suspend fun getMovieAccountStates(movieId: Int): MovieAccountState
    fun getFavoriteMoviesPager(accountId: Int): Flow<PagingData<Movie>>
}
