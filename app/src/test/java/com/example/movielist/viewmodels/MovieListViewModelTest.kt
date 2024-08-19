package com.example.movielist.viewmodels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.movielist.data.MovieListItem
import com.example.movielist.network.NetworkConnectivityObserver
import com.example.movielist.repository.MovieListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class MovieListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: MovieListRepository

    @Mock
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        logMock = mockStatic(Log::class.java)
        logMock.`when`<Boolean> { Log.isLoggable(Mockito.anyString(), Mockito.anyInt()) }
            .thenReturn(false)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `should update movieList when network is connected`() = runTest{
        // Given
        val movieList = listOf(
            MovieListItem(id = 1, title = "Movie 1", posterPath = "/movie1.jpg"),
            MovieListItem(id = 2, title = "Movie 2", posterPath = "/movie2.jpg")
        )
        val pagingData = PagingData.from(movieList)
        val flowPagingData: Flow<PagingData<MovieListItem>> = flowOf(pagingData)
        val differ = AsyncPagingDataDiffer(
            diffCallback = MovieListItemDiffCallback(),
            updateCallback = NoOpListCallback(),
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.IO
        )


        //When
        whenever(networkConnectivityObserver.isConnected).thenReturn(
            MutableStateFlow(true)
        )
        whenever(repository.getMovies()).thenReturn(flowPagingData)
        val viewModel = MovieListViewModel(repository, networkConnectivityObserver)

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        differ.submitData(viewModel.movieList.first())
        println(differ.snapshot())
        assertEquals(movieList, differ.snapshot())
    }

    @Test
    fun `test movies not fetched when disconnected`() = runTest {
        //Given
        val differ = AsyncPagingDataDiffer(
            diffCallback = MovieListItemDiffCallback(),
            updateCallback = NoOpListCallback(),
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.IO
        )

        //When
        whenever(networkConnectivityObserver.isConnected).thenReturn(MutableStateFlow(false))
        val viewModel = MovieListViewModel(repository, networkConnectivityObserver)

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        differ.submitData(viewModel.movieList.first())
        assertEquals(emptyList<MovieListItem>(), differ.snapshot())
    }

    // Helper method to create a PagingDataDiffer for testing
    class MovieListItemDiffCallback : DiffUtil.ItemCallback<MovieListItem>() {
        override fun areItemsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
            return oldItem == newItem
        }
    }

    private class NoOpListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    @After
    fun tearDown() {
        logMock.close()
        Dispatchers.resetMain()
    }
}