package com.example.movielist.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movielist.api.MovieAPI
import com.example.movielist.data.MovieListItem
import javax.inject.Inject

class MoviePagingSource @Inject constructor(private val movieApi: MovieAPI) :
    PagingSource<Int, MovieListItem>() {
    override fun getRefreshKey(state: PagingState<Int, MovieListItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListItem> {
        return try {
            val page = params.key ?: 1
            val response = movieApi.getPopularMovies(page).body()!!.results
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.isEmpty()) null else page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(
                e
            )
        }
    }
}