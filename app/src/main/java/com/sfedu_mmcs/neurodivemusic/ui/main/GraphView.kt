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

    private var xValues: FloatArray? = null
    private var yValues: FloatArray? = null

    fun setFunction(
        X: FloatArray,
        Y: FloatArray,
    ) {
        xValues = X
        yValues = Y
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val yMapRange = width
        val xMapRange = height

        xValues?.let { X ->
            yValues?.let { Y ->
                val xMin = X.min()
                val xMax = X.max()

                val yMin = Y.min()
                val yMax = Y.max()

                var prevX = (xMapRange * (X[0] - xMin) / (xMax - xMin)).toFloat()
                var prevY = (yMapRange * (Y[0] - yMin) / (yMax - yMin)).toFloat()

                for (idx in (1..< X.size)) {
                    val x = X[idx]
                    val y = Y[idx]

                    val mappedX = (xMapRange * (x - xMin) / (xMax - xMin)).toFloat()
                    val mappedY = (yMapRange * (y - yMin) / (yMax - yMin)).toFloat()
                    canvas.drawLine(prevY, prevX, mappedY, mappedX, functionPaint)
                    prevX = mappedX
                    prevY = mappedY
                }
            }
        }
    }
}
