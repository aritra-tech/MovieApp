package com.aritradas.movieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.domain.model.*
import com.aritradas.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val apiServices: ApiServices,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {

    override suspend fun getMovieDetails(movieId: Int): MovieDetail {
        return withContext(ioDispatcher) {
            apiServices.getMovieDetails(movieId)
        }
    }

    override fun getMoviesPager(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(apiServices, query) }
        ).flow
    }

    override suspend fun getAccountDetails(): AccountDetails {
        return withContext(ioDispatcher) {
            apiServices.getAccountDetails()
        }
    }

    override suspend fun addFavorite(
        accountId: Int,
        mediaId: Int,
        isFavorite: Boolean
    ): FavoriteResponse {
        return withContext(ioDispatcher) {
            apiServices.addFavorite(accountId, mediaId, isFavorite)
        }
    }

    override suspend fun getFavoriteMovies(accountId: Int, page: Int): DiscoverMoviesResponse {
        return withContext(ioDispatcher) {
            apiServices.getFavoriteMovies(accountId, page)
        }
    }

    override suspend fun getMovieAccountStates(movieId: Int): MovieAccountState {
        return withContext(ioDispatcher) {
            apiServices.getMovieAccountStates(movieId)
        }
    }

    override fun getFavoriteMoviesPager(accountId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FavoriteMoviesPagingSource(apiServices, accountId) }
        ).flow
    }

    companion object {
        private const val ITEMS_PER_PAGE = 20
    }
}
