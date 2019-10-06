package com.github.jaskelai.chartcustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.TintTypedArray

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var values: Map<String, Int> = HashMap()

    var chartMargins: Int = 0
        set(value) {
            field = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),
                context.resources.displayMetrics
            ).toInt()
            invalidate()
            requestLayout()
        }

    private var maxValue = 0
    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8F

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
            a?.let {
                chartMargins = it.getDimension(R.styleable.ChartView_chartMargins, 0F).toInt()
            }
        } finally {
            a?.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val needWidth = values.size * 60 + paddingStart + paddingEnd + suggestedMinimumWidth
        val needHeight = 200 + paddingBottom + paddingTop + suggestedMinimumHeight

        val measuredWidth = calculateSize(needWidth, widthMeasureSpec)
        val measuredHeight = calculateSize(needHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (values.isNotEmpty()) {
            maxValue = values.maxBy { it.value }?.value ?: 0
            var counter = 0

            val ratio = height / maxValue
            val valueWidth = width / values.size

            for (entry in values) {
                canvas?.drawRect(
                    x + valueWidth * counter,
                    (height - entry.value * ratio).toFloat(),
                    x + valueWidth * counter + valueWidth,
                    height.toFloat(),
                    paint
                )
                counter++
            }
        }
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
