package com.github.jaskelai.chartcustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chartview_main.values =
            mapOf("A" to 700, "B" to 500, "C" to 120, "D" to 300, "E" to 1000)
    }
}
