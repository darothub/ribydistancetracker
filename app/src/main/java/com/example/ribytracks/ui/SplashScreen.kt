package com.example.ribytracks.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ribytracks.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        CoroutineScope(Dispatchers.Main).launch {
            try{
                logo.visibility = View.VISIBLE
                delay(2000)
                logo.visibility = View.GONE
                splash_text.visibility = View.VISIBLE
                delay(1000)
                logo.visibility = View.VISIBLE
                splash_text.visibility = View.GONE
                delay(2000)
                logo.visibility = View.GONE
                splash_text.visibility = View.VISIBLE
                delay(1000)
                logo.visibility = View.VISIBLE
                splash_text.visibility = View.GONE
                delay(2000)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
            catch (e:Exception){

            }
        }
    }
}
