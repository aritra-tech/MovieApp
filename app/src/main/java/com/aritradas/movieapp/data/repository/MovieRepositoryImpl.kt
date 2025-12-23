package com.aritradas.movieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.model.MovieDetail
import com.aritradas.movieapp.domain.model.Genre
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
        val cached = _moviesCache.value.firstOrNull { it.id == movieId }

        return withContext(ioDispatcher) {
            val detail = apiServices.getMovieDetails(movieId)
            val posterUrl = detail.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: cached?.posterUrl
            MovieDetail(
                id = detail.id,
                title = detail.title,
                overview = detail.overview,
                posterUrl = posterUrl,
                rating = detail.voteAverage,
                releaseDate = detail.releaseDate,
                runtime = detail.runtime,
                genres = detail.genres.map { Genre(id = it.id, name = it.name) }
            )
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

