package com.example.movielist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movielist.data.MovieListItem
import com.example.movielist.repository.MovieListRepository
import com.example.movielist.network.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieListRepository,
    networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {
    private val _movieList: MutableStateFlow<PagingData<MovieListItem>> =
        MutableStateFlow(PagingData.empty())
    val movieList: StateFlow<PagingData<MovieListItem>>
        get() = _movieList

    val isConnected = networkConnectivityObserver.isConnected

    private var isFetched = false

    init {
        viewModelScope.launch {
            isConnected.collect{ connected ->
                if(connected && !isFetched) {
                    repository.getMovies()
                        .distinctUntilChanged()
                        .cachedIn(viewModelScope)
                        .collect {
                            _movieList.value = it
                        }
                    isFetched = true
                }
            }
        }
    }
}