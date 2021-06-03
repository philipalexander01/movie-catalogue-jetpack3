package com.example.moviecatalogue.favorite.database

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalogue.favorite.database.entity.Movie

class LocalDataSource private constructor(private val movieDao: MovieDao) {
    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(movieDao: MovieDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(movieDao)
    }

    fun insert(movie: Movie){
        movieDao.insertMovie(movie)
    }

    fun delete(id:Int){
        movieDao.deleteMovie(id)
    }
    fun getMovie(type:String):LiveData<PagedList<com.example.moviecatalogue.model.Movie>>{
       return LivePagedListBuilder(movieDao.getMovie(type),5).build()
    }

    fun getMovieById(id:Int):LiveData<com.example.moviecatalogue.model.Movie>{
        return movieDao.getMovieById(id)
    }
}