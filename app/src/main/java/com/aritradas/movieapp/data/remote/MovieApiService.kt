package com.aritradas.movieapp.data.remote

import com.aritradas.movieapp.domain.model.DiscoverMoviesResponse
import com.aritradas.movieapp.domain.model.MovieDetail
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiServices(
    private val client: HttpClient
) {
    suspend fun discoverMovies(
        page: Int,
    ): DiscoverMoviesResponse {
        val baseUrl = "https://api.themoviedb.org/3/discover/movie"

        return client.get(baseUrl) {
            parameter("include_adult", "false")
            parameter("include_video", "false")
            parameter("language", "en-US")
            parameter("page", page)
            parameter("sort_by", "popularity.desc")
        }.body()
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetail {
        return client.get("https://api.themoviedb.org/3/movie/$movieId") {
            parameter("language", "en-US")
        }.body()
    }
}



