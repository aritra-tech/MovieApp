package com.aritradas.movieapp.ui.navigation

sealed class Screens(val route: String) {
    data object Movies : Screens("movies")
    data object Favourites : Screens("favourites")
    data object Detail : Screens("detail/{movieId}/{posterPath}") {
        fun create(movieId: Int, posterPath: String) = "detail/$movieId/${posterPath.replace("/", "")}"
    }
}