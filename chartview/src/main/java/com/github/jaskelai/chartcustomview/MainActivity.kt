package com.github.jaskelai.chartcustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chartview_main.values =
            mapOf("A" to 20, "B" to 15, "C" to 12, "D" to 3, "E" to 1000)
    }
}
