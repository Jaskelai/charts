package com.github.jaskelai.chartcustomview

import android.graphics.Color

data class ChartData(
    val name: String? = null,
    val value: Int,
    val color: Int = Color.BLACK,
    val isFilled: Boolean = false
)
