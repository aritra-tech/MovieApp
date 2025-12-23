package com.aritradas.movieapp.data.remote

import com.aritradas.movieapp.domain.model.DiscoverMoviesResponse
import com.aritradas.movieapp.domain.model.MovieDetail
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiServices(
    private val client: HttpClient,
    private val apiKeyProvider: () -> String
) {
    suspend fun discoverMovies(
        page: Int,
        query: String?
    ): DiscoverMoviesResponse {
        val apiKey = apiKeyProvider()
        val isSearching = !query.isNullOrBlank()
        val baseUrl = if (isSearching) {
            "https://api.themoviedb.org/3/search/movie"
        } else {
            "https://api.themoviedb.org/3/discover/movie"
        }

        return client.get(baseUrl) {
            parameter("api_key", apiKey)
            parameter("language", "en-US")
            parameter("page", page)
            if (isSearching) {
                parameter("query", query)
            }
        }.body()
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetail {
        val apiKey = apiKeyProvider()
        return client.get("https://api.themoviedb.org/3/movie/$movieId") {
            parameter("api_key", apiKey)
            parameter("language", "en-US")
        }.body()
    }
}



