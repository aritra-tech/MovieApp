package com.aritradas.movieapp.data.remote

import com.aritradas.movieapp.domain.model.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

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

    suspend fun searchMovies(
        query: String,
        page: Int,
    ): DiscoverMoviesResponse {
        val baseUrl = "https://api.themoviedb.org/3/search/movie"

        return client.get(baseUrl) {
            parameter("include_adult", "false")
            parameter("language", "en-US")
            parameter("page", page)
            parameter("query", query)
        }.body()
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetail {
        return client.get("https://api.themoviedb.org/3/movie/$movieId") {
            parameter("language", "en-US")
            parameter("append_to_response", "credits")
        }.body()
    }

    suspend fun getMovieAccountStates(movieId: Int): MovieAccountState {
        return client.get("https://api.themoviedb.org/3/movie/$movieId/account_states").body()
    }

    suspend fun getAccountDetails(): AccountDetails {
        val baseUrl = "https://api.themoviedb.org/3/account"
        return client.get(baseUrl).body()
    }

    suspend fun addFavorite(
        accountId: Int,
        mediaId: Int,
        isFavorite: Boolean
    ): FavoriteResponse {
        val baseUrl = "https://api.themoviedb.org/3/account/$accountId/favorite"
        return client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(
                FavoriteRequest(
                    mediaId = mediaId,
                    favorite = isFavorite
                )
            )
        }.body()
    }

    suspend fun getFavoriteMovies(
        accountId: Int,
        page: Int
    ): DiscoverMoviesResponse {
        val baseUrl = "https://api.themoviedb.org/3/account/$accountId/favorite/movies"
        return client.get(baseUrl) {
            parameter("language", "en-US")
            parameter("page", page)
            parameter("sort_by", "created_at.desc")
        }.body()
    }
}



