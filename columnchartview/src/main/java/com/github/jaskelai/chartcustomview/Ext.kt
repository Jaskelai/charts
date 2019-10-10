package com.github.jaskelai.chartcustomview

fun Map<String, Int>.toChartData(): List<ChartData> {
    return this.map { entry ->
        ChartData(entry.key, entry.value)
    }
}

fun Map<String, Int>.toChartData(color: Int): List<ChartData> {
    return this.map { entry ->
        ChartData(entry.key, entry.value, color)
    }
}

fun Map<String, Int>.toChartData(color: Int, isFilled: Boolean): List<ChartData> {
    return this.map { entry ->
        ChartData(entry.key, entry.value, color, isFilled)
    }
}

fun List<Int>.toChartData(): List<ChartData> {
    return this.map { value ->
        ChartData(value = value)
    }
}

fun List<Int>.toChartData(color: Int): List<ChartData> {
    return this.map { value ->
        ChartData(value = value, color = color)
    }
}

fun List<Int>.toChartData(color: Int, isFilled: Boolean): List<ChartData> {
    return this.map { value ->
        ChartData(value = value, color = color, isFilled = isFilled)
    }
}
