package com.example.moviecatalogue.favorite.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    var id:Int=0,
    var original_name:String?=null,
    var original_title:String?=null,
    var overview:String,
    var poster_path:String,
    var release_date:String?=null,
    var first_air_date:String?=null,
    var vote_average:Double,
    var type:String
    
)
