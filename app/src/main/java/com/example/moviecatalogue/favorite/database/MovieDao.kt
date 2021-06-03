package com.example.moviecatalogue.favorite.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviecatalogue.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie where type = :type ")
    fun getMovie(type: String): androidx.paging.DataSource.Factory<Int,Movie>

    @Query("SELECT * FROM movie where id = :id ")
    fun getMovieById(id: Int): LiveData<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = com.example.moviecatalogue.favorite.database.entity.Movie::class)
    fun insertMovie(movie: com.example.moviecatalogue.favorite.database.entity.Movie)

    @Query("Delete from movie where id = :id")
    fun deleteMovie(id:Int)
}