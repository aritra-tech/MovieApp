package com.aritradas.movieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aritradas.movieapp.data.paging.FavoriteMoviesPagingSource
import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.domain.model.*
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
    private val apiServices: ApiServices,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FavoriteRepository {

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
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FavoriteMoviesPagingSource(apiServices, accountId) }
        ).flow
    }
}
