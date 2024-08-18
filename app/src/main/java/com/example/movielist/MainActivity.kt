package com.example.movielist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movielist.api.MovieAPI
import com.example.movielist.screens.DetailScreen
import com.example.movielist.screens.MainScreen
import com.example.movielist.ui.theme.MovieListTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieListTheme {
                Scaffold { innerPadding ->
                    App(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = "main") {
            MainScreen(onItemClick = { itemId ->
                navController.navigate("detail/$itemId")
            })
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) {
            DetailScreen(onBack = {
                navController.popBackStack()
            })
        }
    }
}
