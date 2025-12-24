package com.aritradas.movieapp.domain.repository

import com.aritradas.movieapp.domain.model.*
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieDetails(movieId: Int): MovieDetail
    fun getMoviesPager(query: String = ""): Flow<PagingData<Movie>>
}