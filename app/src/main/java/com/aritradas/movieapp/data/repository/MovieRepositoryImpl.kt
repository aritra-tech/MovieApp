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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val apiServices: ApiServices,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {

    override suspend fun getMovies(page: Int): List<Movie> {
        return withContext(ioDispatcher) {
            val response = apiServices.discoverMovies(page = page)
            response.results
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetail {
        return withContext(ioDispatcher) {
            apiServices.getMovieDetails(movieId)
        }
    }

}

