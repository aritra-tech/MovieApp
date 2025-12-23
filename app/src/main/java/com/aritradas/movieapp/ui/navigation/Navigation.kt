package com.aritradas.movieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aritradas.movieapp.presentation.movies.MoviesListScreens
import com.aritradas.movieapp.presentation.movies.MoviesViewModel
import com.aritradas.movieapp.presentation.movies.state.MoviesEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    viewModel: MoviesViewModel? = null
) {
    val navController = rememberNavController()
    val vm = viewModel ?: koinViewModel<MoviesViewModel>()

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
//        composable(Screens.Favourites.route) {
//            FavouritesScreen(
//                viewModel = vm,
//                onBack = { navController.popBackStack() },
//                onMovieClick = { movieId ->
//                    navController.navigate(Screens.Detail.create(movieId))
//                }
//            )
//        }
//        composable(
//            route = Screens.Detail.route,
//            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
//            var detailState by remember { mutableStateOf<com.aritradas.movieapp.domain.model.MovieDetail?>(null) }
//            val uiState by vm.uiState.collectAsState()
//
//            LaunchedEffect(movieId) {
//                detailState = vm.loadMovieDetails(movieId)
//            }
//
//            val movieDetail = detailState
//            if (movieDetail != null) {
//                MovieDetailScreen(
//                    movieDetail = movieDetail,
//                    isFavourite = uiState.favourites.contains(movieId),
//                    onBack = { navController.popBackStack() },
//                    onToggleFavourite = { vm.onEvent(MoviesEvent.OnToggleFavourite(movieId)) }
//                )
//            }
//        }
    }
}


