package com.example.movielist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movielist.api.MovieAPI
import com.example.movielist.data.MovieListItem
import com.example.movielist.pagging.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieListRepository @Inject constructor(private val movieAPI: MovieAPI){
    fun getMovies(): Flow<PagingData<MovieListItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = true),
            pagingSourceFactory = {
                MoviePagingSource(movieAPI)
            }
        ).flow
    }
}