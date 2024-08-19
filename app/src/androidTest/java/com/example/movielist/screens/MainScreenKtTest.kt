package com.example.movielist.screens

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.movielist.MainActivity
import com.example.movielist.ui.theme.MovieListTheme
import org.junit.Rule
import org.junit.Test

class MainScreenKtTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testMainScreenDisplaysMovies() {
        composeTestRule.activity.setContent {
            MovieListTheme {
                MainScreen(onItemClick = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("Movie List")
            .assertExists()

        composeTestRule
            .onNodeWithTag("MainScreenGrid")
            .assertExists()
    }


}