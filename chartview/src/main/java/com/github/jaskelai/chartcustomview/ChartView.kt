package com.github.jaskelai.chartcustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.TintTypedArray

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_HEIGHT = 300
        private const val MIN_WIDTH_BY_COLUMN = 72
        private const val STROKE_WIDTH = 4F
        private const val TEXT_SIZE = 48F
    }

    var values: Map<String, Int> = HashMap()
        set(value) {
            field = value
            maxValue = values.maxBy { it.value }?.value ?: 0

            rectTextList.clear()
            rectTextList.addAll(values.keys.map {
                val rect = Rect()
                paint.getTextBounds(it, 0, it.length, rect)
                return@map rect
            })
            calculateTextSizes()

            invalidate()
            requestLayout()
        }

    var chartMargins: Int = 0
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var maxValue = 0
    private var rectTextList = mutableListOf<Rect>()
    private lateinit var paint: Paint
    private lateinit var rectMaxValue: Rect
    private lateinit var rectMinValue: Rect

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
            a?.let {
                chartMargins = it.getDimension(R.styleable.ChartView_chartMargins, 0F).toInt()
            }
        } finally {
            a?.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val needWidth = rectMaxValue.width() + values.size * MIN_WIDTH_BY_COLUMN +
                chartMargins * (values.size - 1) + paddingStart + paddingEnd + suggestedMinimumWidth
        val needHeight = MIN_HEIGHT + paddingBottom + paddingTop + suggestedMinimumHeight

        val measuredWidth = calculateSize(needWidth, widthMeasureSpec)
        val measuredHeight = calculateSize(needHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (values.isNotEmpty()) {
            val lineX = (rectMaxValue.width() * 0.5 + STROKE_WIDTH).toFloat()
            val lineY = (rectMaxValue.height() * 2).toFloat()

            canvas?.drawText(
                maxValue.toString(),
                lineX - rectMaxValue.width() / 2 - STROKE_WIDTH,
                (rectMaxValue.height() * 1.5).toFloat(),
                paint
            )
            canvas?.drawText(
                "0",
                lineX - rectMinValue.width() / 2 - STROKE_WIDTH,
                (height - rectMinValue.height() / 2).toFloat(),
                paint
            )

            val ratio = (height - lineY * 2) / maxValue
            val valueWidth =
                (width - rectMaxValue.width() - (values.size - 1) * chartMargins) / values.size - STROKE_WIDTH

            canvas?.drawLine(
                lineX,
                lineY,
                lineX,
                height - lineY,
                paint
            )

            var counter = 0
            var margin = 0

            for (entry in values) {
                val startX = rectMaxValue.width() + margin * counter + valueWidth * counter

                canvas?.drawRect(
                    startX,
                    height - entry.value * ratio - lineY,
                    startX + valueWidth,
                    height - lineY,
                    paint
                )

                canvas?.drawText(
                    entry.key,
                    startX + valueWidth / 2,
                    (height - rectMinValue.height() / 2).toFloat(),
                    paint
                )
                if (counter == 0) {
                    margin = chartMargins
                }
                counter++
            }
        }
    }

    private fun setupPaint() {
        paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = STROKE_WIDTH
        paint.textSize = TEXT_SIZE
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

    private fun calculateTextSizes() {
        rectMaxValue = Rect()
        paint.getTextBounds(maxValue.toString(), 0, maxValue.toString().length, rectMaxValue)

        rectMinValue = Rect()
        paint.getTextBounds("0", 0, 1, rectMinValue)
    }
}
