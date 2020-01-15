package com.example.ribytracks.utils

import android.app.Application
import com.example.ribytracks.database.TracksDao
import com.example.ribytracks.database.TracksEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoroutineTaskSingleton (application: Application) {

    companion object {
        @Volatile
        private var INSTANCE: CoroutineTaskSingleton? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CoroutineTaskSingleton(application).also {
                    INSTANCE = it
                }
            }
    }

    fun insertTask(tracksDao: TracksDao, tracksEntity: TracksEntity){

        CoroutineScope(Dispatchers.IO).launch {
            tracksDao.insert(tracksEntity)
        }

    }
    fun updateTask(tracksDao: TracksDao, tracksEntity: TracksEntity){

        CoroutineScope(Dispatchers.IO).launch {
            tracksDao.update(tracksEntity)
        }

    }
    fun deleteAllTask(tracksDao: TracksDao){
        CoroutineScope(Dispatchers.IO).launch {
            tracksDao.deleteAllPosts()
        }
    }

}