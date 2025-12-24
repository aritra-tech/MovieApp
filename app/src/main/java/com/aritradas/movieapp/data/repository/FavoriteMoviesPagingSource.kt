package com.aritradas.movieapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.domain.model.Movie

class FavoriteMoviesPagingSource(
    private val apiServices: ApiServices,
    private val accountId: Int
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = apiServices.getFavoriteMovies(accountId = accountId, page = page)
            val movies = response.results

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < (response.totalPages ?: 0)) page + 1 else null
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
