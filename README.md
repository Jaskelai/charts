# charts
[![](https://jitpack.io/v/Jaskelai/column-chart-view.svg)](https://jitpack.io/#Jaskelai/column-chart-view)

Simple custom chart views for Android

<img src="https://github.com/Jaskelai/charts/blob/master/screenshots/Screenshot_1570817065.png" width="200"> <img src="https://github.com/Jaskelai/charts/blob/master/screenshots/Screenshot_1570817477.png" width="200"> <img src="https://github.com/Jaskelai/charts/blob/master/screenshots/Screenshot_1570817652.png" width="200"> <img src="https://github.com/Jaskelai/charts/blob/master/screenshots/Screenshot_1570817652.png" width="200"> <img src="https://github.com/Jaskelai/charts/blob/master/screenshots/Screenshot_1570827569.png" width="200">

# How to add
Add it in your root build.gradle at the end of repositories:
```groovy
dependencies {
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
}
```

Add it in your module build.gradle:
```groovy
dependencies {
    //column chart view
    implementation 'com.github.jaskelai.charts:columnchartview:VERSION'
    //pie chart view
    implementation 'com.github.jaskelai.charts:piechartview:VERSION'
}
```

# How to use
Add the view to layout:
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp" />
```
or
```xml
<com.github.jaskelai.piechartview.PieChartView
            android:id="@+id/chart_pie"
            android:layout_width="300dp"
            android:layout_height="300dp" />
```
# Column chart view
You can convert your list or map to column chart data by library's extensions:
```kotlin
val map: Map<String, Int> = mapOf("AA" to 20, "BB" to 30, "CC" to 100)
val chartDataList: List<ChartData> = map.toChartData()

val list: List<Float> = listOf(20F, 50F, 80F)
val chartDataList: List<ChartData> = list.toChartData()
```   

Set values to column chart:
```kotlin
val map: Map<String, Int> = mapOf("AA" to 20, "BB" to 30, "CC" to 100)
val chartDataList: List<ChartData> = map.toChartData()
chartview_main.values = chartDataList
```    

You can specify margins between columns (0 by default):
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:chartMargins="12dp" />
```

You can specify colors (cyan and pink by default):
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:colorTop="@color/colorPrimary"
	app:colorBottom="@color/colorAccent" />
```

You can specify text size (16sp by default):
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:textSize="20sp" />
```

You can specify filling (false by default):
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:isFilled="true" />
```

# Pie chart view

You can specify pie margins (0 by default):
```xml
<com.github.jaskelai.piechartview.PieChartView
            android:id="@+id/chart_pie"
            android:layout_width="300dp"
            android:layout_height="300dp"
	    app:pieMargins="4dp"/>
```

You can specify radius (0 by default):
```xml
<com.github.jaskelai.piechartview.PieChartView
            android:id="@+id/chart_pie"
            android:layout_width="300dp"
            android:layout_height="300dp"
	    app:radius="4dp"/>
```
