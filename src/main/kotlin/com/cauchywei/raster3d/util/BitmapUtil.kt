package com.cauchywei.raster3d.util

import java.io.File
import java.nio.ByteBuffer

/**
 * Created by cauchywei on 2017/6/8.
 */
object BitmapUtil {


    fun ByteBuffer.put4b(value: Int): ByteBuffer {
        this.put(value.toByte())
        this.put((value ushr 8).toByte())
        this.put((value ushr 16).toByte())
        this.put((value ushr 24).toByte())
        return this
    }

    fun ByteBuffer.put2b(value: Int): ByteBuffer {
        this.put(value.toByte())
        this.put((value ushr 8).toByte())
        return this
    }

    fun ByteArray.write(offset: Int, value: Char): ByteArray {
        this[offset] = value.toByte()
        return this
    }

    fun save(path: String, width: Int, height: Int, dpi: Int, bmp: Array<Color>) {

        val pixelDataHeaderOffset = 54
        val k = width * height
        val s = 24 * k
        val fileSize = pixelDataHeaderOffset + s
        val factor = 39.375
        val m: Int = factor.toInt()

        val ppm = dpi * m


        val file = File(path)
        if (file.exists().not()) {
            if (file.parentFile.exists().not()) {
                file.parentFile.mkdirs()
            }
            file.delete()
            file.createNewFile()
        }

        file.outputStream().use { out ->
            val buffer = ByteBuffer.allocate(1024)

            buffer.apply {
                put('B'.toByte())
                put('M'.toByte())                 // start
                put4b(fileSize)                   // fileSize
                put2b(0)                   // reserved
                put2b(0)                   // reserved
                put4b(pixelDataHeaderOffset)      // pixelOffset

                put4b(40)                         // version name
                put4b(width)                      // width
                put4b(height)                     // height
                put2b(1)                  //plane must be 1
                put2b(24)                 //color depth
                put4b(0)                            //compression method, 0 for none compression
                put4b(s)                            //image size, o for none compression
                put4b(ppm)                          //horizontal resolution
                put4b(ppm)                          //vertical resolution
                put4b(0)                            //number of color palette
                put4b(0)                            //number of important color used

                for (color in bmp) {

                    put((color.b * 255).toByte())
                    put((color.g * 255).toByte())
                    put((color.r * 255).toByte())

                    if (remaining() < 3) {
                        flip()
                        out.write(array(), position(), limit())
                        clear()
                    }
                }

                flip()
                if (limit() > 0)
                    out.write(array(), position(), limit())

                out.flush()
            }

        }
    }


}