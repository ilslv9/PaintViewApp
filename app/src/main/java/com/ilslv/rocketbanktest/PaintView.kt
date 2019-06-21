package com.ilslv.rocketbanktest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PaintView : View {

    private var viewPixelMatrix: Array<Array<Int>>? = null
    private var imageWidth = 0
    private var imageHeight = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = Color.RED

        for (i in 0 until imageHeight) {
            for (j in 0 until imageWidth) {
                if (viewPixelMatrix!![i][j] == 1) {
                    canvas.drawPoint(i.toFloat(), j.toFloat(), paint)
                }
            }
        }
    }

    fun setGenerateImageSize(width: Int, height: Int) {
        if (imageWidth == 0 && imageHeight == 0) {
            imageWidth = width
            imageHeight = height
        }
        if (viewPixelMatrix == null) {
            viewPixelMatrix = Array(height) { emptyArray<Int>() }

            for (i in 0 until viewPixelMatrix!!.size) {
                if (i == 0 || i == viewPixelMatrix!!.size - 1) {
                    viewPixelMatrix!![i] = Array(width) { 1 }
                } else {
                    viewPixelMatrix!![i] = Array(width) { 0 }
                    viewPixelMatrix!![i][0] = 1
                    viewPixelMatrix!![i][height - 1] = 1
                }
            }

            for (i in 1 until height - 1) {
                for (j in 1 until width - 1) {
                    if ((i == j) || (i == height - j - 1)) {
                        viewPixelMatrix!![i][j] = 1
                    } else {
//                    viewPixelMatrix!![i][j] = Random.nextInt(0, 2)
                        viewPixelMatrix!![i][j] = 0
                    }
                }
            }
        }
        invalidate()
    }

    private fun floodFill(x: Float, y: Float) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        if (event.action == MotionEvent.ACTION_DOWN) {

        }
        return super.onTouchEvent(event)
    }
}