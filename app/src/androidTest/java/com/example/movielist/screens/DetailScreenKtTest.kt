package com.example.movielist.screens

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.movielist.MainActivity
import com.example.movielist.data.Genre
import com.example.movielist.data.MovieDetail
import org.junit.Rule
import org.junit.Test

class DetailScreenKtTest{
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val movieDetail = MovieDetail(
        id = 533535,
        title = "Deadpool & Wolverine",
        backdropPath = "/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg",
        runtime = 128,
        rating = 7.812,
        releaseDate = "2024-07-24",
        genres = listOf(
            Genre(28, "Action"),
            Genre(358, "Comedy"),
            Genre(878, "Science Fiction")
        ),
        overview = "A listless Wade Wilson toils away in civilian life with his days as the morally flexible mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade must reluctantly suit-up again with an even more reluctant Wolverine."
    )

    @Test
    fun testDetailsScreenDisplaysMovieDetails(){
        composeTestRule.activity.setContent {
            DetailScreenContent(movieDetail, onBack = { })
        }

        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithTag("banner")
            .assertExists()

        composeTestRule
            .onNodeWithText(movieDetail.title)
            .assertExists()

        composeTestRule
            .onNodeWithText("${movieDetail.runtime} minutes")
            .assertExists()

        composeTestRule
            .onNodeWithText(movieDetail.rating.toString())
            .assertExists()

        composeTestRule
            .onNodeWithText(movieDetail.releaseDate)
            .assertExists()

        composeTestRule
            .onNodeWithText(movieDetail.genres.first().name)
            .assertExists()

        composeTestRule
            .onNodeWithText(movieDetail.overview)
            .assertExists()
    }
}