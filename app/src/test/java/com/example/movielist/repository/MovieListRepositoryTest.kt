package com.example.movielist.repository

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.movielist.api.MovieAPI
import com.example.movielist.data.MovieListItem
import com.example.movielist.data.MovieResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import retrofit2.Response

class MovieListRepositoryTest {

    @Mock
    private lateinit var mockApi: MovieAPI

    private lateinit var repository: MovieListRepository

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        logMock = mockStatic(Log::class.java)
        logMock.`when`<Boolean> { Log.isLoggable(Mockito.anyString(), Mockito.anyInt()) }
            .thenReturn(false)
        repository = MovieListRepository(mockApi)
    }

    @Test
    fun `getMovies returns expected PagingData`() = runTest(testDispatcher) {
        // Given
        val movieList = listOf(
            MovieListItem(id = 1, title = "Movie 1", posterPath = "/movie1.jpg"),
            MovieListItem(id = 2, title = "Movie 2", posterPath = "/movie2.jpg")
        )

        //When
        Mockito.`when`(mockApi.getPopularMovies(any()))
            .thenReturn(Response.success(MovieResponse(1, movieList)))
        val result: Flow<PagingData<MovieListItem>> = repository.getMovies()
        val snapshot = result.asSnapshot {  }
            .take(movieList.size)

        // Then
        assertEquals(movieList, snapshot)
    }

    @After
    fun tearDown() {
        logMock.close()
    }
}