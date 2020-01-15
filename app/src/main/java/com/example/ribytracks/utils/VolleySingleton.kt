package com.example.ribytracks.utils


import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton(context: Context) {

    companion object{
        @Volatile
        private var newInstance: VolleySingleton? = null

        fun getInstance(context: Context) =
            newInstance?: synchronized(this){
                newInstance ?: VolleySingleton(context).also{
                    newInstance = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy{
        Volley.newRequestQueue(context.applicationContext)
    }

    fun<T> addToRequestQueue(req: Request<T>){
        requestQueue.add(req)
    }
}