package com.example.moviecatalogue.favorite.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalogue.favorite.database.LocalDataSource
import com.example.moviecatalogue.favorite.database.MovieDao
import com.example.moviecatalogue.model.Movie
import com.example.moviecatalogue.utils.EspressoIdlingResource
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalMovieRepository(private val localDataSource: LocalDataSource) {
    companion object {
        @Volatile
        private var instance: LocalMovieRepository? = null
        fun getInstance(localDataSource: LocalDataSource): LocalMovieRepository =
                instance ?: synchronized(this) {
                    LocalMovieRepository(localDataSource).apply { instance = this }
                }
    }

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun getAllMovie(type: String): LiveData<PagedList<Movie>> = localDataSource.getMovie(type)

    fun getMovieById(id: Int): LiveData<Movie> = localDataSource.getMovieById(id)

    fun insert(movie: com.example.moviecatalogue.favorite.database.entity.Movie) {
        EspressoIdlingResource.increment()
        executorService.execute {
            localDataSource.insert(movie)
            EspressoIdlingResource.decrement()
        }
    }

    fun delete(id: Int) {
        EspressoIdlingResource.increment()
        executorService.execute {
            localDataSource.delete(id)
            EspressoIdlingResource.decrement()
        }
    }

}