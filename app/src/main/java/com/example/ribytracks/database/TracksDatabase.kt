package com.example.ribytracks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TracksEntity::class], version = 1, exportSchema = false)
abstract class TracksDatabase:RoomDatabase() {
    abstract fun tracksDao(): TracksDao

    companion object {
        private var instance: TracksDatabase? = null
        @kotlin.jvm.Synchronized
        fun getInstance(context: Context): TracksDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    TracksDatabase::class.java, "track_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}