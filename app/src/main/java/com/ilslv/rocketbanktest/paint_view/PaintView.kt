package com.ilslv.rocketbanktest.paint_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.Rig
import com.stedi.randomimagegenerator.callbacks.GenerateCallback
import com.stedi.randomimagegenerator.generators.ColoredRectangleGenerator
import com.stedi.randomimagegenerator.generators.effects.ThresholdEffect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*


class PaintView : View {

    /**
     * Coefficient for animation speed
     */
    var animationCoefficient = 0f
    /**
     * Algorithm which visualisation is presented
     */
    var fillType: AlgorithmType = AlgorithmType.QUEUE

    /**
     * Bitmap width
     */
    private var imageWidth = 0
    /**
     * Bitmap height
     */
    private var imageHeight = 0

    /**
     * Generated bitmap
     */
    private var image: Bitmap? = null

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

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = Color.RED
        canvas.drawBitmap(image, 0f, 0f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        if (event.action == MotionEvent.ACTION_DOWN) {
            when (fillType) {
                AlgorithmType.QUEUE -> {
                    GlobalScope.async {
                        floodFill(x.toInt(), y.toInt())
                    }
                }
                AlgorithmType.LINEAR -> {
                    GlobalScope.async {
                        linearFill(x.toInt(), y.toInt(), image!!)
                    }
                }
                AlgorithmType.RECURSIVE -> {
                    GlobalScope.async {
                        recursiveFill(x.toInt(), y.toInt())
                    }
                }
            }

        }
        return super.onTouchEvent(event)
    }

    /**
     * Regenerating view
     * @param width new view width
     * @param height new view height
     */
    fun setGenerateImageSize(width: Int, height: Int) {
        imageWidth = width
        imageHeight = height

        Rig.Builder()
            .setGenerator(ThresholdEffect(ColoredRectangleGenerator(10)))
            .setFixedSize(imageWidth, imageHeight)
            .setCallback(object : GenerateCallback {
                override fun onGenerated(imageParams: ImageParams, bitmap: Bitmap) {
                    image = bitmap
                }

                override fun onFailedToGenerate(imageParams: ImageParams, e: Exception) {

                }
            })
            .build()
            .generate()

    }

    /**
     * Queue algorithm
     */
    private fun floodFill(x: Int, y: Int) {
        val queue = LinkedList<Point>()
        queue.add(Point(y, x))
        while (!queue.isEmpty()) {
            val p = queue.remove()
            if (p.x >= 0 && (p.x < image!!.width && p.y >= 0 && p.y < image!!.height)) {
                if (image!!.getPixel(p.x, p.y) == Color.WHITE) {
                    image!!.setPixel(p.x, p.y, Color.BLACK)
                    queue.add(Point(p.x + 1, p.y))
                    queue.add(Point(p.x - 1, p.y))
                    queue.add(Point(p.x, p.y + 1))
                    queue.add(Point(p.x, p.y - 1))
                    invalidate()
                }
            }
        }
    }

    /**
     * Linear algorithm
     */
    private fun linearFill(x: Int, y: Int, bmp: Bitmap) {
        val pixelQueue = LinkedList<Point>()
        pixelQueue.add(Point(x, y))
        while (pixelQueue.size > 0) {
            val n = pixelQueue.poll()
            if (bmp.getPixel(n.x, n.y) != Color.WHITE)
                continue

            val nextPoint = Point(n.x + 1, n.y)

            while (n.x > 0 && bmp.getPixel(n.x, n.y) == Color.WHITE) {
                bmp.setPixel(n.x, n.y, Color.BLACK)
                invalidate()
                if (n.y > 0 && bmp.getPixel(n.x, n.y - 1) == Color.WHITE)
                    pixelQueue.add(Point(n.x, n.y - 1))
                if (n.y < bmp.height - 1 && bmp.getPixel(n.x, n.y + 1) == Color.WHITE)
                    pixelQueue.add(Point(n.x, n.y + 1))

                n.x--
            }

            while (nextPoint.x < bmp.width - 1 && bmp.getPixel(nextPoint.x, nextPoint.y) == Color.WHITE) {
                bmp.setPixel(nextPoint.x, nextPoint.y, Color.BLACK)
                invalidate()
                if (nextPoint.y > 0 && bmp.getPixel(nextPoint.x, nextPoint.y - 1) == Color.WHITE)
                    pixelQueue.add(Point(nextPoint.x, nextPoint.y - 1))
                if (nextPoint.y < bmp.height - 1 && bmp.getPixel(nextPoint.x, nextPoint.y + 1) == Color.BLACK)
                    pixelQueue.add(Point(nextPoint.x, nextPoint.y + 1))

                nextPoint.x++
            }
        }
    }

    /**
     * Recursive algorithm
     */
    private fun recursiveFill(x: Int, y: Int) {
        val currentColor = image!!.getPixel(x, y)
        if (currentColor == Color.WHITE) {
            image!!.setPixel(x, y, Color.BLACK)
            invalidate()
            recursiveFill(x + 1, y)
            recursiveFill(x - 1, y)
            recursiveFill(x, y + 1)
            recursiveFill(x, y - 1)
        }
    }
}
