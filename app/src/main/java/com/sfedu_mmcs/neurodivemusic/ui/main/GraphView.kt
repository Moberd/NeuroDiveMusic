package com.sfedu_mmcs.neurodivemusic.ui.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

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

        val centerX = width / 2f
        val centerY = height / 2f

        // Draw X and Y axes
        canvas.drawLine(0f, centerY, width.toFloat(), centerY, axisPaint)
        canvas.drawLine(centerX, 0f, centerX, height.toFloat(), axisPaint)

        // Draw function graph
        function?.let { func ->
            val stepSize = 1f
            var prevX = 0f
            var prevY = func(prevX)
            var x = stepSize

            while (x <= width) {
                val y = func(x)
                canvas.drawLine(prevX, centerY - prevY, x, centerY - y, functionPaint)
                prevX = x
                prevY = y
                x += stepSize
            }
        }
    }
}
