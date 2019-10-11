# column-chart-view
[![](https://jitpack.io/v/Jaskelai/column-chart-view.svg)](https://jitpack.io/#Jaskelai/column-chart-view)

Simple custom column chart view for Android

<img src="https://github.com/Jaskelai/charts/blob/master/screenshots/Screenshot_1570817065.png" width="200"> <img src="https://github.com/Jaskelai/charts/blob/master/screenshots/Screenshot_1570817477.png" width="200"> <img src="https://github.com/Jaskelai/charts/blob/master/screenshots/Screenshot_1570817652.png" width="200">

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
    implementation 'com.github.jaskelai:charts:VERSION'
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

You can convert your list or map to chart data by library's extensions:
```kotlin
val map: Map<String, Int> = mapOf("AA" to 20, "BB" to 30, "CC" to 100)
val chartDataList: List<ChartData> = map.toChartData()

val list: List<Float> = listOf(20F, 50F, 80F)
val chartDataList: List<ChartData> = list.toChartData()
```   

Set values to chart:
```kotlin
val map: Map<String, Int> = mapOf("AA" to 20, "BB" to 30, "CC" to 100)
val chartDataList: List<ChartData> = map.toChartData()
chartview_main.values = chartDataList
```    

# Margins
You can specify margins between columns:
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:chartMargins="12dp" />
```

# Colors
You can specify colors:
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:colorTop="@color/colorPrimary"
	app:colorBottom="@color/colorAccent" />
```

# Text size
You can specify text size:
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:textSize="20sp" />
```

# Filling
You can specify filling:
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:isFilled="true" />
```
