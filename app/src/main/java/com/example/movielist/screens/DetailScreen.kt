package com.example.movielist.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.movielist.data.Genre
import com.example.movielist.data.MovieDetail
import com.example.movielist.viewmodels.MovieDetailViewModel

@Composable
fun DetailScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: MovieDetailViewModel = hiltViewModel()
    val movieDetail = viewModel.movieDetail.collectAsState()

    movieDetail.value?.let { detail ->
        DetailScreenContent(movieDetail = detail, onBack = onBack, modifier = modifier)
    }
}

@Composable
fun DetailScreenContent(
    movieDetail: MovieDetail,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ImageWithBackButton(
            url = movieDetail.backdropPath,
            onBack = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = modifier.height(16.dp))
        Column(modifier = modifier.padding(horizontal = 8.dp)) {
            Text(
                text = movieDetail.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            WatchTimeAndRating(time = movieDetail.runtime, rating = movieDetail.rating)
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReleaseDate(date = movieDetail.releaseDate, modifier = Modifier.weight(1f))
                GenreContent(genres = movieDetail.genres, modifier = Modifier.weight(2f))
            }
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Summary(summary = movieDetail.overview)
        }
    }
}

@Composable
fun WatchTimeAndRating(time: Int, rating: Double, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Timer, contentDescription = "Watch Time")
        Spacer(modifier = modifier.width(8.dp))
        Text(text = "$time minutes", textAlign = TextAlign.Center)
        Spacer(modifier = modifier.width(14.dp))
        Icon(Icons.Filled.Star, contentDescription = "Rating")
        Spacer(modifier = modifier.width(4.dp))
        Text(text = rating.toString())
    }
}

@Composable
fun ImageWithBackButton(
    url: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500$url",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = BrushPainter(
                Brush.linearGradient(
                    listOf(Color.White, Color.LightGray)
                )
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(50.dp))
                .clip(RoundedCornerShape(50.dp))
        ) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
        }
    }
}

@Composable
fun ReleaseDate(date: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Release Date",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = date,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}

@Composable
fun GenreContent(genres: List<Genre>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Genre",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(genres) { genre ->
                RoundedCornerText(text = genre.name)
            }
        }
    }
}

@Composable
fun Summary(summary: String, modifier: Modifier = Modifier) {
    Text(
        text = "Summary",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
    Spacer(modifier = modifier.height(8.dp))
    Text(text = summary)
}

@Composable
fun RoundedCornerText(text: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.LightGray,
        modifier = modifier
            .padding(4.dp)
    ) {
        Text(
            text = text,
            color = Color.Black,
            modifier = Modifier.padding(8.dp),
            maxLines = 1
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun DetailScreenPreview() {
    val movieDetail = MovieDetail(
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
    DetailScreenContent(movieDetail = movieDetail, onBack = {})
}
