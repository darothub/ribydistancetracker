package com.example.ribytracks.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TracksDao {

    @Insert
    suspend fun insert(tracksEntity: TracksEntity)

    @Update
    suspend fun update(tracksEntity: TracksEntity)

    @Query("DELETE FROM tracksentity")
    suspend fun deleteAllPosts()

    @get:Query(
        "SELECT * FROM tracksentity ORDER BY id ASC"
    )
    val allTracks: LiveData<List<TracksEntity?>?>?
}