package com.ilslv.rocketbanktest

import android.content.Context
import android.graphics.Point
import android.util.TypedValue
import android.view.WindowManager


fun Context.getScreenSize(): Pair<Int, Int> {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return Pair(size.x, size.y)
}

fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics)
        .toInt()
}