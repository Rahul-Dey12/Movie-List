package com.example.movielist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.movielist.data.MovieListItem
import com.example.movielist.viewmodels.MovieListViewModel

@Composable
fun MainScreen(onItemClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val viewModel: MovieListViewModel = hiltViewModel()
    val movieList = viewModel.movieList.collectAsLazyPagingItems()
    val isConnected = viewModel.isConnected.collectAsState()
    if(isConnected.value) {
        MainScreenContent(movieList, onItemClick, modifier)
    }else{
        NoInternetScreen()
    }
}

@Composable
fun MainScreenContent(
    movieList: LazyPagingItems<MovieListItem>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Header()
        Spacer(modifier = Modifier.height(16.dp))
        MainScreenGrid(movieList, onItemClick)
    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    Text(
        text = "Movie List",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun MainScreenGrid(
    movieList: LazyPagingItems<MovieListItem>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        items(movieList.itemCount) { index ->
            movieList[index]?.let {
                MovieItem(it, onItemClick)
            }
        }
    }
}

@Composable
fun MovieItem(item: MovieListItem, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(item.id) }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w200${item.posterPath}",
            contentDescription = null,
            placeholder = BrushPainter(
                Brush.linearGradient(
                    listOf(Color.White, Color.LightGray)
                )
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(8.dp)
        )
        Text(
            text = item.title,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
