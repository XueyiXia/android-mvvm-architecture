package com.jetpack.mvvm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jetpack.mvvm.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}