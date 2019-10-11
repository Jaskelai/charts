package com.github.jaskelai.chartcustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Path
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.DashPathEffect
import android.graphics.Shader
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
        private const val MARGIN_AXIS_COLUMN = 36F
        private const val GUIDELINES_COLOR = "#A0A0A0"
        private const val DEFAULT_TOP_COLOR = Color.CYAN
        private const val DEFAULT_BOTTOM_COLOR = "#ff99cc"
    }

    var values = emptyList<ChartData>()
        set(values) {
            field = values

            maxValue = values.maxBy { it.value }?.value ?: 0F
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

    var textSize: Int = 48

    var colorTop: Int = DEFAULT_TOP_COLOR

    var colorBottom: Int = Color.parseColor(DEFAULT_BOTTOM_COLOR)

    var isFilled: Boolean = false

    private var maxValue = 0F
    private var minValue = 0F
    private val rectMaxValue = Rect()
    private val rectMiddleValue = Rect()
    private val rectMinValue = Rect()

    private val columnPaint = Paint()
    private val textPaint = Paint()
    private val guidelinesPaint = Paint()

    private val constraintPath = Path()

    init {
        setupColumnPaint()
        setupTextPaint()
        setupGuidelinesPaint()

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
                textSize = it.getDimension(R.styleable.ColumnChartView_textSize, 48F).toInt()
                colorTop = it.getColor(R.styleable.ColumnChartView_colorTop, DEFAULT_TOP_COLOR)
                colorBottom = it.getColor(
                    R.styleable.ColumnChartView_colorBottom,
                    Color.parseColor(DEFAULT_BOTTOM_COLOR)
                )
                isFilled = it.getBoolean(R.styleable.ColumnChartView_isFilled, false)
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

        val axisStartX = paddingStart.toFloat()
        val axisTopY = (paddingTop + rectMaxValue.height()).toFloat()
        val axisBottomY = (height - paddingBottom - rectMinValue.height() * 2).toFloat()
        val pxPerUnit = (axisBottomY - axisTopY) / maxValue
        val columnWidth = (width - paddingStart - paddingEnd - rectMaxValue.width() -
                (values.size - 1) * chartMargins - MARGIN_AXIS_COLUMN) / values.size

        var margin = 0

        for ((counter, entry) in values.withIndex()) {
            val startX =
                paddingStart + rectMaxValue.width() + margin * counter + columnWidth * counter

            val top = if (entry.value > 0) {
                axisBottomY - entry.value * pxPerUnit
            } else {
                height - axisTopY - STROKE_WIDTH / 2
            }

            drawColumn(canvas, startX + MARGIN_AXIS_COLUMN, columnWidth, top, axisBottomY)

            drawColumnTitle(canvas, entry, startX, columnWidth, axisBottomY)

            drawAxis(canvas, axisStartX, axisTopY, axisBottomY)

            if (counter == 0) {
                margin = chartMargins
            }
        }
    }

    private fun setupColumnPaint() {
        columnPaint.run {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
            textSize = this@ColumnChartView.textSize.toFloat()
            color = Color.BLACK
        }
    }

    private fun setupTextPaint() {
        textPaint.run {
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
            textSize = this@ColumnChartView.textSize.toFloat()
            color = Color.BLACK
        }
    }

    private fun setupGuidelinesPaint() {
        guidelinesPaint.run {
            guidelinesPaint.style = Paint.Style.STROKE
            guidelinesPaint.strokeWidth = 4F
            guidelinesPaint.color = Color.parseColor(GUIDELINES_COLOR)
            guidelinesPaint.pathEffect = DashPathEffect(floatArrayOf(8F, 8F), 0F)
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
        textPaint.getTextBounds(maxValue.toString(), 0, maxValue.toString().length, rectMaxValue)

        textPaint.getTextBounds(
            (maxValue / 2).toString(),
            0,
            (maxValue / 2).toString().length,
            rectMiddleValue
        )

        textPaint.getTextBounds(minValue.toString(), 0, minValue.toString().length, rectMinValue)
    }

    private fun drawAxis(canvas: Canvas?, axisX: Float, axisTopY: Float, axisBottomY: Float) {
        canvas?.drawText(
            maxValue.toString(),
            axisX,
            axisTopY + rectMaxValue.height() / 2,
            textPaint
        )
        constraintPath.moveTo(rectMaxValue.width() + MARGIN_AXIS_COLUMN, axisTopY)
        constraintPath.lineTo((width - paddingEnd).toFloat(), axisTopY)
        canvas?.drawPath(constraintPath, guidelinesPaint)

        canvas?.drawText(
            (maxValue / 2).toString(),
            axisX + rectMaxValue.width() / 2 - rectMiddleValue.width() / 2,
            (axisBottomY - axisTopY) / 2 + rectMiddleValue.height() / 2,
            textPaint
        )
        constraintPath.moveTo(
            rectMaxValue.width() + MARGIN_AXIS_COLUMN,
            (axisBottomY - axisTopY) / 2
        )
        constraintPath.lineTo((width - paddingEnd).toFloat(), (axisBottomY - axisTopY) / 2)
        canvas?.drawPath(constraintPath, guidelinesPaint)

        canvas?.drawText(
            minValue.toString(),
            axisX + rectMaxValue.width() / 2 - rectMinValue.width() / 2,
            axisBottomY + rectMinValue.height() / 2,
            textPaint
        )
        constraintPath.moveTo(
            rectMaxValue.width() + MARGIN_AXIS_COLUMN,
            axisBottomY
        )
        constraintPath.lineTo((width - paddingEnd).toFloat(), axisBottomY)
        canvas?.drawPath(constraintPath, guidelinesPaint)
    }

    private fun drawColumn(
        canvas: Canvas?,
        startX: Float,
        columnWidth: Float,
        top: Float,
        bottom: Float
    ) {
        columnPaint.color = colorTop
        val colors = intArrayOf(colorTop, colorBottom)
        val gradient = LinearGradient(
            startX + columnWidth / 2,
            top,
            startX + columnWidth / 2,
            bottom,
            colors,
            null,
            Shader.TileMode.CLAMP
        )
        columnPaint.shader = gradient

        if (isFilled) {
            columnPaint.style = Paint.Style.FILL
        } else {
            columnPaint.style = Paint.Style.STROKE
        }

        canvas?.drawRect(
            startX,
            top,
            startX + columnWidth,
            bottom,
            columnPaint
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
                startX + columnWidth / 2 - textPaint.measureText(it) / 2 + MARGIN_AXIS_COLUMN,
                axisBottomY + rectMinValue.height() * 1.5F,
                textPaint
            )
        }
    }
}
