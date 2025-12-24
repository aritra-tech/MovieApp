package com.aritradas.movieapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverMoviesResponse(
    @SerialName("page") val page: Int? = null,
    @SerialName("results") val results: List<Movie> = emptyList(),
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null
)
