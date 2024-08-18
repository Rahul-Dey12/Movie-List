package com.example.movielist.repository

import com.example.movielist.api.MovieAPI
import com.example.movielist.data.MovieDetail
import javax.inject.Inject

class MovieDetailRepository @Inject constructor(private val movieAPI: MovieAPI) {
    suspend fun getMovieDetail(id: Int) : MovieDetail?{
        return kotlin.runCatching {
            val response = movieAPI.getMovieDetails(id)
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                null
            }
        }.getOrNull()
    }
}