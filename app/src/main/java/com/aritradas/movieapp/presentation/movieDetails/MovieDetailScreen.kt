package com.aritradas.movieapp.presentation.movieDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aritradas.movieapp.domain.model.MovieDetail
import com.aritradas.movieapp.presentation.movieDetails.state.MovieDetailState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    viewModel: MovieDetailViewModel = koinViewModel(),
    onFavoriteToggle: () -> Unit = {}
) {
    val currentState by viewModel.state.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    when (val state = currentState) {
        is MovieDetailState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F0D13)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFE91E63))
            }
        }

        is MovieDetailState.Success -> {
            MovieDetailContent(
                movieDetail = state.movieDetail,
                isFavorite = state.isFavorite,
                onBack = onBack,
                onFavoriteClick = {
                    viewModel.toggleFavorite(movieId)
                    onFavoriteToggle()
                }
            )
        }

        is MovieDetailState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F0D13)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun MovieDetailContent(
    movieDetail: MovieDetail,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0D13))
    ) {
        AsyncImage(
            model = IMAGE_BASE_URL + movieDetail.posterPath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF0F0D13)
                        ),
                        startY = 0f,
                        endY = 1200f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(350.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(32.dp),
                color = Color.White.copy(alpha = 0.1f),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = movieDetail.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val year = movieDetail.releaseDate?.take(4) ?: ""
                        val runtime = movieDetail.runtime?.let { "${it / 60}h ${it % 60}m" } ?: ""

                        InfoChip(
                            text = year,
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                        Text("•", color = Color.White.copy(alpha = 0.4f))
                        InfoChip(
                            text = runtime,
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                        Text("•", color = Color.White.copy(alpha = 0.4f))

                        movieDetail.genres.firstOrNull()?.let { genre ->
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF673AB7).copy(alpha = 0.3f),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.1f)
                                )
                            ) {
                                Text(
                                    text = genre.name,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFFB39DDB)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFE91E63), Color(0xFFFF4081))
                                )
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Text(
                                text = "Play Now",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DetailSection(title = "Synopsis", content = movieDetail.overview)

            Spacer(modifier = Modifier.height(32.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = String.format("%.1f", movieDetail.voteAverage),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
    }
}

@Composable
fun InfoChip(text: String, icon: @Composable (() -> Unit)?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        icon?.invoke()
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun DetailSection(title: String, content: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.White.copy(alpha = 0.05f),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            )
        }
    }
}
