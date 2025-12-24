package com.aritradas.movieapp.data.repository

import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.domain.model.AccountDetails
import com.aritradas.movieapp.domain.model.FavoriteResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryImplTest {

    private lateinit var repository: FavoriteRepositoryImpl
    private val apiServices: ApiServices = mockk()

    @Before
    fun setup() {
        repository = FavoriteRepositoryImpl(apiServices)
    }

    @Test
    fun `getAccountDetails returns data from api`() = runTest {
        val expected = AccountDetails(id = 1, username = "Test User")
        coEvery { apiServices.getAccountDetails() } returns expected

        val result = repository.getAccountDetails()

        assertEquals(expected, result)
    }

    @Test
    fun `addFavorite calls api with correct parameters`() = runTest {
        val accountId = 1
        val mediaId = 123
        val isFavorite = true
        val expected = mockk<FavoriteResponse>()
        coEvery { apiServices.addFavorite(accountId, mediaId, isFavorite) } returns expected

        val result = repository.addFavorite(accountId, mediaId, isFavorite)

        assertEquals(expected, result)
    }
}
