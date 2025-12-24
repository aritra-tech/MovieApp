package com.aritradas.movieapp.domain.repository

import com.aritradas.movieapp.domain.model.*
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieDetails(movieId: Int): MovieDetail
    fun getMoviesPager(query: String = ""): Flow<PagingData<Movie>>
    suspend fun getAccountDetails(): AccountDetails
    suspend fun addFavorite(accountId: Int, mediaId: Int, isFavorite: Boolean): FavoriteResponse
    suspend fun getFavoriteMovies(accountId: Int, page: Int): DiscoverMoviesResponse
    suspend fun getMovieAccountStates(movieId: Int): MovieAccountState
    fun getFavoriteMoviesPager(accountId: Int): Flow<PagingData<Movie>>
}