package com.github.jaskelai.chartcustomview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.TintTypedArray
import java.util.*

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var segments: List<SegmentData> = listOf()

    var values: Map<String, Int> = hashMapOf()
        set(value) {
            field = value

            val valuesSum = value.map { it.value }.sum()
            segments = value.map {
                SegmentData(
                    it.key,
                    it.value.getPercentOf(valuesSum),
                    generateColor()
                )
            }

            invalidate()
            requestLayout()
        }

    var pieMargins: Float = 0F
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var radius: Float = 0F
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private val oval: RectF = RectF()
    private val outerOval: RectF = RectF()
    private val paint: Paint = Paint()

    init {
        paint.color = Color.BLUE
        paint.strokeWidth = 14F
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        val a = attrs?.let {
            TintTypedArray.obtainStyledAttributes(
                context,
                attrs,
                R.styleable.PieChartView,
                defStyleAttr,
                defStyleAttr
            )
        }

        try {
            a?.let {
                pieMargins = it.getDimension(R.styleable.PieChartView_pieMargins, 0F)
                radius = it.getDimension(R.styleable.PieChartView_radius, 0F)
            }
        } finally {
            a?.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size: Int
        val width = measuredWidth
        val height = measuredHeight
        val widthWithoutPadding = width - paddingLeft - paddingRight
        val heightWithoutPadding = height - paddingTop - paddingBottom

        size = if (widthWithoutPadding > heightWithoutPadding) {
            heightWithoutPadding
        } else {
            widthWithoutPadding
        }

        setMeasuredDimension(
            size + paddingLeft + paddingRight,
            size + paddingTop + paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        oval.set(
            0F + paddingStart,
            0F + paddingTop,
            width.toFloat() - paddingEnd,
            height.toFloat() - paddingBottom
        )
        outerOval.set(
            paddingStart - pieMargins,
            paddingTop - pieMargins,
            width.toFloat() - paddingEnd + pieMargins,
            height.toFloat() - paddingBottom + pieMargins
        )

        var start = 0F
        paint.strokeWidth = 4F
        paint.style = Paint.Style.FILL
        for (segment in segments) {
            paint.color = segment.color ?: Color.RED
            val sweep = 360F * segment.value
            canvas?.drawArc(oval, start, sweep, true, paint)
            start += sweep
        }

        start = 0F
        paint.strokeWidth = pieMargins
        paint.style = Paint.Style.STROKE
        paint.color = Color.WHITE
        for (segment in segments) {
            val sweep = 360F * segment.value
            canvas?.drawArc(outerOval, start, sweep, true, paint)
            start += sweep
        }
        paint.style = Paint.Style.FILL
        paint.color = (background as? ColorDrawable)?.color ?: Color.WHITE
        canvas?.drawCircle(oval.centerX(), oval.centerY(), radius / 2, paint)
    }

    private fun generateColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    private fun Int.getPercentOf(sum: Int): Float {
        if (sum == 0) return 0F
        return 100F / sum * this / 100F
    }
}
