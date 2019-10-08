package com.github.jaskelai.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chartview_main.values = mapOf("AA" to 20, "BB" to 50, "CC" to 105, "DD" to 45)
    }
}
