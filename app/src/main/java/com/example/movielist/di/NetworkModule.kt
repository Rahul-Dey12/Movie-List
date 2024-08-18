package com.example.movielist.di

import android.content.Context
import com.example.movielist.network.ApiKeyInterceptor
import com.example.movielist.api.MovieAPI
import com.example.movielist.network.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiKey(): String =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlZDc1MjA4MGMxMGNlN2VkNzdmYmFlZDkzNWU2NTM3ZSIsIm5iZiI6MTcyMzg3OTM4MC4wNDM5ODQsInN1YiI6IjY2YzAzODEwNGNiYjg0MjIyYzlkYzM2NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ok3N7qyJCVoRyp6qPFIVk5UxgwKguazR4L3Us1HwDyk"

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(apiKey: String): ApiKeyInterceptor {
        return ApiKeyInterceptor(apiKey)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieAPI(retrofit: Retrofit): MovieAPI {
        return retrofit.create(MovieAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }
}