package com.example.moviecatalogue.di

import android.app.Application
import com.example.moviecatalogue.favorite.database.LocalDataSource
import com.example.moviecatalogue.favorite.database.MovieRoomDatabase
import com.example.moviecatalogue.favorite.repository.LocalMovieRepository
import com.example.moviecatalogue.model.RemoteDataSource
import com.example.moviecatalogue.repository.MovieRepository

object Injection {
    fun provideRepository(): MovieRepository {
        val remoteRepository = RemoteDataSource().getInstance()
        return MovieRepository.getInstance(remoteRepository)
    }

    fun provideRepositoryLocal(application: Application): LocalMovieRepository {
        val database = MovieRoomDatabase.getInstance(application)
        val localDataSource = LocalDataSource.getInstance(database.movieDao())
        return LocalMovieRepository.getInstance(localDataSource)
    }
}