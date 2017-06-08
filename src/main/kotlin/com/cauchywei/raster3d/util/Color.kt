package com.cauchywei.raster3d.util

/**
 * Created by cauchywei on 2017/6/8.
 */
class Color(val r: Float, val g: Float, val b: Float) {
    companion object {
        @JvmField
        val BLACK = Color(0.0f, 0.0f, 0.0f)
        @JvmField
        val WHITE = Color(1f, 1f, 1f)
        @JvmField
        val RED = Color(1f, 0f, 0f)
        @JvmField
        val GREEN = Color(0f, 1f, 0f)
        @JvmField
        val BLUE = Color(0f, 0f, 1f)
    }
}