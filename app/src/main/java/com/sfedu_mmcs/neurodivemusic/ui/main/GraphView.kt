package com.sfedu_mmcs.neurodivemusic.ui.main
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

class GraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    private val functionPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private var function: ((Float) -> Float)? = null

    fun setFunction(func: (Float) -> Float) {
        function = func
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val yMapRange = width
        val xMapRange = height

        val xValues = FloatArray(100) { it / 10f } // Generates x values from 0.0 to 9.9
        val yValues = xValues.map { sin(it) } // Calculates y values using the sine function

        val yMin = yValues.min()
        val yMax = yValues.max()

        val xMin = xValues.min()
        val xMax = xValues.max()

        var prevX = (xMapRange * (xValues[0] - xMin) / (xMax - xMin)).toFloat()
        var prevY = (yMapRange * (yValues[0] - yMin) / (yMax - yMin)).toFloat()

        for (idx in (1..<xValues.size)) {
            val x = xValues[idx]
            val y = yValues[idx]

            val mappedX = (xMapRange * (x - xMin) / (xMax - xMin)).toFloat()
            val mappedY = (yMapRange * (y - yMin) / (yMax - yMin)).toFloat()
            canvas.drawLine(prevY, prevX, mappedY, mappedX, functionPaint)
            prevX = mappedX
            prevY = mappedY
        }
    }
}
