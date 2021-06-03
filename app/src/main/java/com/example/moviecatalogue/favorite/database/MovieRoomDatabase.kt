package com.example.moviecatalogue.favorite.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviecatalogue.favorite.database.entity.Movie

@Database(
    entities = [Movie::class],
    version = 1
)
abstract class MovieRoomDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        fun getInstance(context: Context): MovieRoomDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MovieRoomDatabase::class.java,
                    "Movie_db"
                ).allowMainThreadQueries().build().apply {
                    INSTANCE = this
                }
            }
    }
    abstract fun movieDao():MovieDao

}