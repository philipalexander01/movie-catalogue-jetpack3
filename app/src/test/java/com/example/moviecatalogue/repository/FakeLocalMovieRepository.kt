package com.example.moviecatalogue.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalogue.favorite.database.LocalDataSource
import com.example.moviecatalogue.favorite.database.MovieDao
import com.example.moviecatalogue.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FakeLocalMovieRepository (private val localDataSource: LocalDataSource) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun getAllMovie(type: String): LiveData<PagedList<Movie>> = localDataSource.getMovie(type)

    fun getMovieById(id: Int): LiveData<Movie> = localDataSource.getMovieById(id)

    fun insert(movie: com.example.moviecatalogue.favorite.database.entity.Movie) {
        executorService.execute {
            localDataSource.insert(movie)
        }
    }

    fun delete(id: Int) {
        executorService.execute {
            localDataSource.delete(id)
        }
    }

}