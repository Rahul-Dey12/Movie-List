package com.example.movielist.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.MovieDetail
import com.example.movielist.repository.MovieDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?>
        get() = _movieDetail


    init {
        viewModelScope.launch {
            val movieId = savedStateHandle.get<Int>("id") ?: return@launch
            _movieDetail.emit(repository.getMovieDetail(movieId))
        }
    }
}