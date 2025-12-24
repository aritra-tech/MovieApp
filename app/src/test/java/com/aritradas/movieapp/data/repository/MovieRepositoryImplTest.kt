package com.aritradas.movieapp.data.repository

import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.domain.model.MovieDetail
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepositoryImpl
    private val apiServices: ApiServices = mockk()

    @Before
    fun setup() {
        repository = MovieRepositoryImpl(apiServices)
    }

    @Test
    fun `getMovieDetails returns correct detail from api`() = runTest {
        // Arrange
        val movieId = 123
        val expectedDetail = mockk<MovieDetail>()
        coEvery { apiServices.getMovieDetails(movieId) } returns expectedDetail

        // Act
        val result = repository.getMovieDetails(movieId)

        // Assert
        assertEquals(expectedDetail, result)
    }

    @Test(expected = Exception::class)
    fun `getMovieDetails throws exception when api fails`() = runTest {
        // Arrange
        val movieId = 123
        coEvery { apiServices.getMovieDetails(movieId) } throws Exception("API Error")

        // Act
        repository.getMovieDetails(movieId)
        
        // Assert: Expected exception
    }
}
