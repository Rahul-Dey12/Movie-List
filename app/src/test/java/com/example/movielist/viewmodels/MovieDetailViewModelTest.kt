package com.example.movielist.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.movielist.data.MovieDetail
import com.example.movielist.repository.MovieDetailRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MovieDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: MovieDetailRepository

    private lateinit var savedStateHandle: SavedStateHandle

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savedStateHandle = SavedStateHandle(mapOf("id" to 1))
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `movieDetail is updated when repository returns data`() = runTest {
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
        Mockito.`when`(repository.getMovieDetail(1)).thenReturn(movieDetail)

        //When
        val viewModel = MovieDetailViewModel(repository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.movieDetail.first()
        assertEquals(movieDetail, result)
    }

    @Test
    fun `movieDetail remains null when repository returns null`() = runTest {
        // Given
        Mockito.`when`(repository.getMovieDetail(1)).thenReturn(null)

        // When
        val viewModel = MovieDetailViewModel(repository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.movieDetail.first()
        assertEquals(null, result)
    }

    @After
    fun  tearDown(){
        Dispatchers.resetMain()
    }
}