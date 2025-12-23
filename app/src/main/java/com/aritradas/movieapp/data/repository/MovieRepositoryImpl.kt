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

    // Cache of movies we have loaded through paging, used for details/favourites screens.
    private val _moviesCache = MutableStateFlow<List<Movie>>(emptyList())

    private val _favourites = MutableStateFlow<Set<Int>>(emptySet())
    override val favourites: Flow<Set<Int>> = _favourites.asStateFlow()

    override fun getPagedMovies(query: String?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(
                    apiServices = apiServices,
                    query = query,
                    onPageLoaded = { movies ->
                        _moviesCache.update { current ->
                            (current + movies).distinctBy { it.id }
                        }
                    }
                )
            }
        ).flow
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetail {
        return withContext(ioDispatcher) {
            apiServices.getMovieDetails(movieId)
        }
    }

    override suspend fun toggleFavourite(movieId: Int) {
        withContext(ioDispatcher) {
            val current = _favourites.value
            _favourites.value = if (current.contains(movieId)) {
                current - movieId
            } else {
                current + movieId
            }
        }
    }
}

