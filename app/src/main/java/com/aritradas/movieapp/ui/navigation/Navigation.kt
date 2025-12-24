package com.aritradas.movieapp.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aritradas.movieapp.presentation.favourites.FavouritesScreen
import com.aritradas.movieapp.presentation.favourites.FavouritesViewModel
import com.aritradas.movieapp.presentation.movieDetails.MovieDetailScreen
import com.aritradas.movieapp.presentation.movies.MoviesListScreens
import com.aritradas.movieapp.presentation.movies.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    viewModel: MoviesViewModel? = null
) {
    val navController = rememberNavController()
    val vm = viewModel ?: koinViewModel<MoviesViewModel>()
    val favouritesViewModel: FavouritesViewModel = koinViewModel()

    @OptIn(ExperimentalSharedTransitionApi::class)
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Screens.Movies.route
        ) {
            composable(Screens.Movies.route) {
                MoviesListScreens(
                    viewModel = vm,
                    onMovieClick = { movieId, posterPath ->
                        navController.navigate(Screens.Detail.create(movieId, posterPath))
                    },
                    onFavouritesClick = {
                        navController.navigate(Screens.Favourites.route)
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                )
            }
            composable(Screens.Favourites.route) {
                FavouritesScreen(
                    viewModel = favouritesViewModel,
                    onBack = { navController.popBackStack() },
                    onMovieClick = { movieId, posterPath ->
                        navController.navigate(Screens.Detail.create(movieId, posterPath))
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                )
            }
            composable(
                route = Screens.Detail.route,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType },
                    navArgument("posterPath") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                val posterPath = backStackEntry.arguments?.getString("posterPath") ?: ""

                MovieDetailScreen(
                    movieId = movieId,
                    initialPosterPath = posterPath,
                    onBack = { navController.popBackStack() },
                    onFavoriteToggle = {
                        vm.toggleFavorite(movieId)
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                )
            }
        }
    }
}


