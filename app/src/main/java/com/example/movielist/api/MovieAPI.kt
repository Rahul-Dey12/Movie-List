package com.example.movielist.api

import com.example.movielist.data.MovieDetail
import com.example.movielist.data.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") movieId: Int): Response<MovieDetail>
}