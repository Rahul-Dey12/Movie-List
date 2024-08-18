package com.example.movielist.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetail(
    val id: Int,
    val title: String,
    @Json(name = "backdrop_path")
    val backdropPath: String,
    val runtime: Int,
    @Json(name = "vote_average")
    val rating: Double,
    @Json(name = "release_date")
    val releaseDate: String,
    val genres: List<Genre>,
    val overview: String
)

@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int,
    val name: String
)