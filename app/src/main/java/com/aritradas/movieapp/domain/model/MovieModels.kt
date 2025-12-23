package com.aritradas.movieapp.domain.model

/**
 * Domain model for a movie in the list/grid view.
 * Derived from TMDB DiscoverMovieDto.
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val rating: Double,
    val releaseYear: String?
)

/**
 * Domain model for detailed movie information.
 * Derived from TMDB MovieDetailResponse.
 */
data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val rating: Double,
    val releaseDate: String?,
    val runtime: Int?,
    val genres: List<Genre>
)

/**
 * Domain model for movie genre.
 */
data class Genre(
    val id: Int,
    val name: String
)

