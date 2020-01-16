package com.example.ribytracks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.ribytracks.database.TracksEntity
import com.example.ribytracks.database.TracksRepository

class TracksViewModel(application: Application):AndroidViewModel(application) {
    private var repository = TracksRepository(application)
    private var allTracks = repository.allTracks

    fun getAllTracks():LiveData<List<TracksEntity?>?>?{
        return allTracks
    }

    fun insert(tracksEntity: TracksEntity, application: Application){
        repository.insert(tracksEntity, application)
    }

    fun update(tracksEntity: TracksEntity, application: Application){
        repository.update(tracksEntity, application)
    }

    fun deleteAll(application: Application){
        repository.deleteAll(application)
    }
}