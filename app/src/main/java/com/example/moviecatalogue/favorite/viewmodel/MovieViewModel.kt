package com.example.moviecatalogue.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviecatalogue.favorite.repository.LocalMovieRepository
import com.example.moviecatalogue.model.Movie

class MovieViewModel(private val mLocalMovieRepository: LocalMovieRepository) : ViewModel(){

    fun getDataMovie(type:String):LiveData<PagedList<Movie>> = mLocalMovieRepository.getAllMovie(type)

    fun getDataMovieById(movieId:Int):LiveData<Movie> = mLocalMovieRepository.getMovieById(movieId)

    fun insert(movie: com.example.moviecatalogue.favorite.database.entity.Movie) {
        mLocalMovieRepository.insert(movie)
    }

    fun delete(id: Int){
        mLocalMovieRepository.delete(id)
    }
}