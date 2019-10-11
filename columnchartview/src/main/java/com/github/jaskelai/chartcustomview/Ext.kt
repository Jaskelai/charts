package com.github.jaskelai.chartcustomview

fun Map<String, Number>.toChartData(): List<ChartData> {
    return this.map { entry ->
        ChartData(entry.key, entry.value.toFloat())
    }
}

fun List<Number>.toChartData(): List<ChartData> {
    return this.map { value ->
        ChartData(value = value.toFloat())
    }
}
