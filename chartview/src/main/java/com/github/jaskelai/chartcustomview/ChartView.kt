package com.github.jaskelai.chartcustomview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.TintTypedArray

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var values: Map<String, Int> = HashMap()

    var chartMargins: Int? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    init {
        setupPaint()

        val a = attrs?.let {
            TintTypedArray.obtainStyledAttributes(
                context,
                attrs,
                R.styleable.ChartView,
                defStyleAttr,
                defStyleAttr
            )
        }

        try {
            chartMargins = a?.getInteger(R.styleable.ChartView_chartMargins, 0)
        } finally {
            a?.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val needWidth = paddingStart + paddingEnd + suggestedMinimumWidth
        val needHeight = paddingBottom + paddingTop + suggestedMinimumHeight

        val measuredWidth = calculateSize(needWidth, widthMeasureSpec)
        val measuredHeight = calculateSize(needHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun setupPaint() {

    }

    private fun calculateSize(contentSize: Int, measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return when (mode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> if (contentSize < specSize) contentSize else specSize
            else -> contentSize
        }
    }
}
