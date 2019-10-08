# column-chart-view
[![](https://jitpack.io/v/Jaskelai/column-chart-view.svg)](https://jitpack.io/#Jaskelai/column-chart-view)

Simple custom column chart view for Android

<img src="https://github.com/Jaskelai/column-chart-view/blob/master/screenshots/Screenshot_1570545373.png" width="200">

# How to add
Add the dependency in your build.gradle:
```groovy
dependencies {
    implementation 'com.github.jaskelai:column-chart-view:VERSION'
}
```

# How to use
Add the view to layout:
```xml
<com.github.jaskelai.chartcustomview.ColumnChartView
        android:id="@+id/chartview_main"
        android:layout_width="300dp"
        android:layout_height="300dp"/>
```

Set values to chart:
```kotlin
chartview_main.values = mapOf("AA" to 20, "BB" to 50, "CC" to 105, "DD" to 45)
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
