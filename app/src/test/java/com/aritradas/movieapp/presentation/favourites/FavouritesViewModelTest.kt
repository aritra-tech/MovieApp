package com.aritradas.movieapp.presentation.favourites

import app.cash.turbine.test
import com.aritradas.movieapp.domain.model.AccountDetails
import com.aritradas.movieapp.domain.model.DiscoverMoviesResponse
import com.aritradas.movieapp.domain.repository.FavoriteRepository
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
class FavouritesViewModelTest {

    private val favoriteRepository: FavoriteRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: FavouritesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock account details and favorites for initialization
        coEvery { favoriteRepository.getAccountDetails() } returns AccountDetails(id = 1)
        coEvery { favoriteRepository.getFavoriteMovies(1, 1) } returns DiscoverMoviesResponse(results = emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization fetches account and favorites`() = runTest(testDispatcher) {
        viewModel = FavouritesViewModel(favoriteRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { favoriteRepository.getAccountDetails() }
        coVerify { favoriteRepository.getFavoriteMovies(1, 1) }
    }

    @Test
    fun `onSearch updates searchQuery state`() = runTest {
        viewModel = FavouritesViewModel(favoriteRepository)
        val query = "Avengers"
        viewModel.onSearch(query)

        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `toggleFavorite interacts with repository and updates UI state`() = runTest(testDispatcher) {
        viewModel = FavouritesViewModel(favoriteRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val movieId = 456
        coEvery { favoriteRepository.addFavorite(1, movieId, true) } returns mockk()

        viewModel.toggleFavorite(movieId)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assert(state.favourites.contains(movieId))
            coVerify { favoriteRepository.addFavorite(1, movieId, true) }
        }
    }
}
