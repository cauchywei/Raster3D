package com.cauchywei.raster3d.math

import mapInPlace


/**
 * Created by cauchywei on 2017/6/8.
 */

class Vec(vararg element: Double) : AbstractMatrix(), Iterable<MatrixType> {


    override fun iterator(): Iterator<MatrixType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val x get() = array[0]
    val y get() = array[1]
    val z get() = array[2]
    val w get() = array[3]

    val magnitude: Double
        get() {
            var sum: Double = 0.0
            forEach { sum += it * it }
            return sum
        }


    val array: ArrayList<Double> = ArrayList(element.map { it })

    fun normalize(): Vec {
        val magnitude = magnitude

        if (magnitude > 0) {
            array.mapInPlace { it / magnitude }
        }
        return this
    }

    operator fun get(index: Int): MatrixType = array[index]


    override fun get(x: Int, y: Int): MatrixType {
        return array[y]
    }

    operator fun times(rhs: Vec): MatrixType {
        matchCheck(rhs)
        return array.zip(rhs.array) { a, b -> a * b }.sum()
    }

    override val cols: Int
        get() = 1
    override val rows: Int
        get() = array.size

}

inline fun Vec.forEachIndexed(action: (Int, MatrixType) -> Unit): Unit {
    for (x in 0..rows - 1) {
        action(x, this[x])
    }
}

fun vec3Of(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = Vec(x, y, z)
fun vec4Of(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, w: Double = 0.0) = Vec(x, y, z, w)


