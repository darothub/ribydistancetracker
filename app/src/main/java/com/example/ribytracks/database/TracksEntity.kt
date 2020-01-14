package com.example.ribytracks.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class TracksEntity(
    val startCoordinates:List<String>,
    val endCoordinates:List<String>

):Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var distance:Int = 0
}

