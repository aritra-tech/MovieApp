package com.aritradas.movieapp.presentation.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.aritradas.movieapp.presentation.movies.ErrorMessage
import com.aritradas.movieapp.presentation.movies.MovieGrid
import com.aritradas.movieapp.presentation.favourites.FavouritesViewModel
@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel,
    onBack: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val favoriteMovies = viewModel.favoriteMoviesPager.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Favourites",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                when {
                    favoriteMovies.loadState.refresh is LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color(0xFFE91E63)
                        )
                    }

                    favoriteMovies.loadState.refresh is LoadState.Error -> {
                        val error = (favoriteMovies.loadState.refresh as LoadState.Error).error
                        ErrorMessage(
                            message = error.message ?: "Unknown error",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    favoriteMovies.itemCount > 0 -> {
                        val uiState by viewModel.uiState.collectAsState()
                        MovieGrid(
                            moviePager = favoriteMovies,
                            isFavorite = { id -> uiState.favourites.contains(id) },
                            onFavoriteClick = { id -> viewModel.toggleFavorite(id) },
                            onMovieClick = onMovieClick
                        )
                    }

                    favoriteMovies.loadState.refresh is LoadState.NotLoading && favoriteMovies.itemCount == 0 -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No favourites yet",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Movies you like will appear here",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}
