package com.example.movielist.repository

import com.example.movielist.api.MovieAPI
import com.example.movielist.data.MovieDetail
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class MovieDetailRepositoryTest {

    @Mock
    private lateinit var mockApi: MovieAPI

    private lateinit var movieDetailRepository: MovieDetailRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        movieDetailRepository = MovieDetailRepository(mockApi)
    }

    @Test
    fun `getMovieDetail returns MovieDetail when response is successful`() = runBlocking {
        // Given
        val movieDetail = MovieDetail(
            id = 1,
            title = "Sample Movie",
            backdropPath = "/sample.jpg",
            runtime = 120,
            rating = 7.5,
            releaseDate = "2024-08-18",
            genres = listOf(),
            overview = ""
        )
        val response = Response.success(movieDetail)
        Mockito.`when`(mockApi.getMovieDetails(1)).thenReturn(response)

        // When
        val result = movieDetailRepository.getMovieDetail(1)

        // Then
        assertEquals(movieDetail, result)
    }

    @Test
    fun `getMovieDetail returns null when response is unsuccessful`() = runBlocking {
        // Given
        val response = Response.error<MovieDetail>(404, "Error".toResponseBody(null))
        Mockito.`when`(mockApi.getMovieDetails(1)).thenReturn(response)

        // When
        val result = movieDetailRepository.getMovieDetail(1)

        // Then
        assertEquals(null, result)
    }

    @Test
    fun `getMovieDetail returns null when an exception occurs`() = runBlocking {
        // Given
        Mockito.`when`(mockApi.getMovieDetails(1)).thenThrow(RuntimeException("Network Error"))

        // When
        val result = movieDetailRepository.getMovieDetail(1)

        // Then
        assertEquals(null, result)
    }
}