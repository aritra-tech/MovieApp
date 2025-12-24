package com.aritradas.movieapp.di

import android.util.Log
import com.aritradas.movieapp.BuildConfig
import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.data.repository.MovieRepositoryImpl
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.presentation.movies.MoviesViewModel
import com.aritradas.movieapp.presentation.movieDetails.MovieDetailViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        val bearerToken = BuildConfig.TMDB_BEARER_TOKEN

        HttpClient(Android) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KtorClient", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = true
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }

            defaultRequest {
                header("Authorization", "Bearer $bearerToken")
                header("Accept", "application/json")
            }

            engine {
                connectTimeout = 30_000
                socketTimeout = 30_000
                proxy = null
            }
        }
    }

    single {
        ApiServices(client = get())
    }

    single<MovieRepository> {
        MovieRepositoryImpl(apiServices = get())
    }

    viewModel {
        MoviesViewModel(repository = get())
    }

    viewModel {
        MovieDetailViewModel(repository = get())
    }
}