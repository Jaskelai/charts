package com.github.jaskelai.sample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.jaskelai.chartcustomview.ChartData
import com.github.jaskelai.chartcustomview.toChartData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val map: Map<String, Int> = mapOf("AA" to 20, "BB" to 30, "CC" to 100)
        val chartDataList: List<ChartData> = map.toChartData(Color.CYAN, true)
        chartview_main.values = chartDataList
    }
}
