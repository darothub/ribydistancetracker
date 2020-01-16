package com.example.ribytracks.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class TracksEntity(
    val startPointLat:String,
    val startPointLong:String,
    val stopPointLat:String,
    val stopPointLong:String,
    val date:String

):Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var distance:String?=null
}

