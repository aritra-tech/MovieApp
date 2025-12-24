package com.aritradas.movieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aritradas.movieapp.presentation.movies.MoviesListScreens
import com.aritradas.movieapp.presentation.favourites.FavouritesScreen
import com.aritradas.movieapp.presentation.movies.MoviesViewModel
import com.aritradas.movieapp.presentation.movieDetails.MovieDetailScreen
import com.aritradas.movieapp.presentation.favourites.FavouritesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    viewModel: MoviesViewModel? = null
) {
    val navController = rememberNavController()
    val vm = viewModel ?: koinViewModel<MoviesViewModel>()
    val favouritesViewModel: FavouritesViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.Movies.route
    ) {
        composable(Screens.Movies.route) {
            MoviesListScreens(
                viewModel = vm,
                onMovieClick = { movieId ->
                    navController.navigate(Screens.Detail.create(movieId))
                },
                onFavouritesClick = {
                    navController.navigate(Screens.Favourites.route)
                }
            )
        }
        composable(Screens.Favourites.route) {
            FavouritesScreen(
                viewModel = favouritesViewModel,
                onBack = { navController.popBackStack() },
                onMovieClick = { movieId ->
                    navController.navigate(Screens.Detail.create(movieId))
                }
            )
        }
        composable(
            route = Screens.Detail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            
            MovieDetailScreen(
                movieId = movieId,
                onBack = { navController.popBackStack() },
                onFavoriteToggle = {
                    vm.toggleFavorite(movieId)
                }
            )
        }
    }
}


