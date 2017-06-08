package com.cauchywei.raster3d

import com.cauchywei.raster3d.util.BitmapUtil
import com.cauchywei.raster3d.util.Color
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.on

/**
 * Created by cauchywei on 2017/6/8.
 */
object Test : Spek({

    given("a bitmap array") {
        val width = 640
        val height = 480
        val dpi = 72

        on("write a white bmp") {

            val bmp = Array(width * height) {
                Color.WHITE
            }
            BitmapUtil.save("gen/WHITE.bmp",width,height,dpi,bmp)

        }


        on("write a black bmp") {

            val bmp = Array(width * height) {
                Color.BLACK
            }
            BitmapUtil.save("gen/black.bmp",width,height,dpi,bmp)

        }


        on("write a green bmp") {

            val bmp = Array(width * height) {
                Color.GREEN
            }
            BitmapUtil.save("gen/green.bmp",width,height,dpi,bmp)

        }


    }
})