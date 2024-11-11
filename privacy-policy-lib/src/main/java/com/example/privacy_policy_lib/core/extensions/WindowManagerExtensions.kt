package com.example.privacy_policy_lib.core.extensions

import android.graphics.Point
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager

fun WindowManager?.getUsableScreenHeight(): Int {
    if(this == null) return 0
    val height: Int
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = this.currentWindowMetrics
        val windowInsets: WindowInsets = windowMetrics.windowInsets

        val insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout())

        val insetsHeight = insets.top + insets.bottom

        val b = windowMetrics.bounds
        height = b.height() - insetsHeight

    } else {
        val size = Point()
        @Suppress("DEPRECATION")
        val display = this.defaultDisplay // deprecated in API 30
        @Suppress("DEPRECATION")
        display?.getSize(size) // deprecated in API 30
        //width = size.x
        height = size.y
    }
    return height
}