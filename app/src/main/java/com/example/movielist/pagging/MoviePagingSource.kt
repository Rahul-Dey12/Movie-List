package com.example.movielist.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movielist.api.MovieAPI
import com.example.movielist.data.MovieListItem
import javax.inject.Inject

class MoviePagingSource @Inject constructor(private val movieApi: MovieAPI) :
    PagingSource<Int, MovieListItem>() {
    override fun getRefreshKey(state: PagingState<Int, MovieListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListItem> {
        return try {
            val page = params.key ?: 1
            val response = movieApi.getPopularMovies(page)
            if (response.isSuccessful && response.body() != null) {
                val movieList = response.body()!!.results
                LoadResult.Page(
                    data = movieList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (movieList.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("API call failed or body is null"))
            }
        } catch (e: Exception) {
            LoadResult.Error(
                e
            )
        }
    }
}