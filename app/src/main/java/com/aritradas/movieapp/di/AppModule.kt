package com.aritradas.movieapp.di

import com.aritradas.movieapp.BuildConfig
import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.data.repository.MovieRepositoryImpl
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.presentation.movies.MoviesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
        }
    }

    single {
        ApiServices(
            client = get(),
            apiKeyProvider = {
                BuildConfig.TMDB_API_KEY
            }
        )
    }

    single<MovieRepository> { MovieRepositoryImpl(apiServices = get()) }

    viewModel { MoviesViewModel(repository = get()) }
}


