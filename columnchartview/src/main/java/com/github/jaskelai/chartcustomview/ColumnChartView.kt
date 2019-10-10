package com.github.jaskelai.chartcustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.TintTypedArray

class ColumnChartView @JvmOverloads constructor(
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

    var values = emptyList<ChartData>()
        set(values) {
            field = values

            maxValue = values.maxBy { it.value }?.value ?: 0
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
    private val rectMaxValue = Rect()
    private val rectMinValue = Rect()

    private val paint = Paint()
    private val textPaint = Paint()

    init {
        setupPaint()
        setupTextPaint()

        val array = attrs?.let {
            TintTypedArray.obtainStyledAttributes(
                context,
                attrs,
                R.styleable.ColumnChartView,
                defStyleAttr,
                defStyleAttr
            )
        }
        try {
            array?.let {
                chartMargins = it.getDimension(R.styleable.ColumnChartView_chartMargins, 0F).toInt()
            }
        } finally {
            array?.recycle()
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

        if (values.isEmpty()) {
            return
        }

        val axisX = rectMaxValue.width() * 0.5F + STROKE_WIDTH + paddingStart
        val axisTopY = rectMaxValue.height() * 2F + paddingTop
        val axisBottomY = height - paddingBottom - rectMinValue.height() * 2 - STROKE_WIDTH
        val ratio = (axisBottomY - axisTopY) / maxValue
        val columnWidth = (width - paddingStart - paddingEnd - rectMaxValue.width() -
                (values.size - 1) * chartMargins) / values.size - STROKE_WIDTH

        drawAxis(canvas, axisX, axisTopY, axisBottomY)

        var margin = 0

        for ((counter, entry) in values.withIndex()) {
            val startX =
                paddingStart + rectMaxValue.width() + margin * counter + columnWidth * counter

            val top = if (entry.value > 0) {
                axisBottomY - entry.value * ratio
            } else {
                height - axisTopY - STROKE_WIDTH / 2
            }

            drawColumn(canvas, entry, startX, columnWidth, top, axisBottomY)

            drawColumnTitle(canvas, entry, startX, columnWidth, axisBottomY)

            if (counter == 0) {
                margin = chartMargins
            }
        }
    }

    private fun setupPaint() {
        paint.run {
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
            textSize = TEXT_SIZE
        }
    }

    private fun setupTextPaint() {
        textPaint.run {
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
            textSize = TEXT_SIZE
            color = Color.BLACK
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

    private fun calculateTextSizes() {
        paint.getTextBounds(maxValue.toString(), 0, maxValue.toString().length, rectMaxValue)

        paint.getTextBounds("0", 0, 1, rectMinValue)
    }

    private fun drawAxis(canvas: Canvas?, axisX: Float, axisTopY: Float, axisBottomY: Float) {
        canvas?.drawText(
            maxValue.toString(),
            axisX - rectMaxValue.width() / 2 - STROKE_WIDTH,
            axisTopY - rectMaxValue.height() / 2,
            textPaint
        )

        canvas?.drawText(
            "0",
            axisX - rectMinValue.width() / 2 - STROKE_WIDTH,
            axisBottomY + rectMinValue.height() * 1.5F + STROKE_WIDTH,
            textPaint
        )

        canvas?.drawLine(
            axisX,
            axisTopY,
            axisX,
            axisBottomY,
            textPaint
        )
    }

    private fun drawColumn(
        canvas: Canvas?,
        entry: ChartData,
        startX: Float,
        columnWidth: Float,
        top: Float,
        bottom: Float
    ) {
        paint.color = entry.color
        if (entry.isFilled) {
            paint.style = Paint.Style.FILL
        } else {
            paint.style = Paint.Style.STROKE
        }

        canvas?.drawRect(
            startX,
            top,
            startX + columnWidth,
            bottom,
            paint
        )
    }

    private fun drawColumnTitle(
        canvas: Canvas?,
        chartData: ChartData,
        startX: Float,
        columnWidth: Float,
        axisBottomY: Float
    ) {
        chartData.name?.let {
            canvas?.drawText(
                it,
                startX + columnWidth / 2 - textPaint.measureText(it) / 2,
                axisBottomY + rectMinValue.height() * 1.5F + STROKE_WIDTH,
                textPaint
            )
        }
    }
}
