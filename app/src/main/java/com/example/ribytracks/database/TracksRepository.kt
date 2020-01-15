package com.example.ribytracks.database

import android.app.Application
import com.example.ribytracks.utils.CoroutineTaskSingleton

//class TracksRepository(application: Application) {
//
//    private val database = TracksDatabase.getInstance(application)
//
//    private val tracksDao:TracksDao =database!!.tracksDao()
//    var allTracks =tracksDao.allTracks
//
//    fun insert(tracksEntity: TracksEntity, application: Application){
//        CoroutineTaskSingleton.getInstance(application).insertTask(tracksDao, tracksEntity)
//    }
//
//    fun update(tracksEntity: TracksEntity, application: Application){
//        CoroutineTaskSingleton.getInstance(application).updateTask(tracksDao, tracksEntity)
//    }
//
//    fun deleteAll(application: Application){
//        CoroutineTaskSingleton.getInstance(application).deleteAllTask(tracksDao)
//    }
//
//}