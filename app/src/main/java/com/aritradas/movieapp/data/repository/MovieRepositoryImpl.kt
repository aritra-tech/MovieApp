package com.aritradas.movieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.model.MovieDetail
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

    override fun getMoviesPager(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(apiServices) }
        ).flow
    }

    companion object {
        private const val ITEMS_PER_PAGE = 20
    }
}
