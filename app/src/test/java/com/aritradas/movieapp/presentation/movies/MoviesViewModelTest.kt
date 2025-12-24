package com.aritradas.movieapp.presentation.movies

import app.cash.turbine.test
import com.aritradas.movieapp.domain.model.AccountDetails
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import com.aritradas.movieapp.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val favoriteRepository: FavoriteRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock account details for initialization
        coEvery { favoriteRepository.getAccountDetails() } returns AccountDetails(id = 1)
        
        viewModel = MoviesViewModel(movieRepository, favoriteRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearch updates searchQuery state`() = runTest {
        // Act
        val query = "Inception"
        viewModel.onSearch(query)

        // Assert
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `toggleFavorite adds to favorites if not present`() = runTest(testDispatcher) {
        // Arrange
        val movieId = 123
        coEvery { favoriteRepository.addFavorite(any(), movieId, true) } returns mockk()

        // Act
        viewModel.toggleFavorite(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.favourites.contains(movieId))
            coVerify { favoriteRepository.addFavorite(1, movieId, true) }
        }
    }

    @Test
    fun `toggleFavorite removes from favorites if already present`() = runTest(testDispatcher) {
        // Arrange
        val movieId = 123
        // First add it
        viewModel.toggleFavorite(movieId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        coEvery { favoriteRepository.addFavorite(any(), movieId, false) } returns mockk()

        // Act
        viewModel.toggleFavorite(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assert(!state.favourites.contains(movieId))
            coVerify { favoriteRepository.addFavorite(1, movieId, false) }
        }
    }
}
