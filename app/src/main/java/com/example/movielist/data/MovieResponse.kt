package com.example.movielist.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class MovieResponse(
    val page: Int,
    val results: List<MovieListItem>
)

@JsonClass(generateAdapter = true)
data class MovieListItem(
    val id: Int,
    val title: String,
    @Json(name = "poster_path")
    val posterPath: String,
)