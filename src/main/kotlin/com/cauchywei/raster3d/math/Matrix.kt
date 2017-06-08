package com.cauchywei.raster3d.math

/**
 * Created by cauchywei on 2017/6/8.
 */

import java.util.ArrayList

typealias MatrixType = Double

interface Matrix {
    val cols: Int
    val rows: Int

    operator fun get(x: Int, y: Int): MatrixType
}

val Matrix.size: Int
    get() = this.cols * this.rows

interface MutableMatrix : Matrix {
    operator fun set(x: Int, y: Int, value: MatrixType)
}

abstract class AbstractMatrix : Matrix {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append('[')
        forEachIndexed { x, y, value ->
            if (x == 0)
                sb.append('[')
            sb.append(value.toString())
            if (x == cols - 1) {
                sb.append(']')
                if (y < rows - 1)
                    sb.append(", ")
            } else {
                sb.append(", ")
            }
        }
        sb.append(']')
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Matrix) return false
        if (rows != other.rows || cols != other.cols) return false

        var eq = true
        forEachIndexed { x, y, value ->
            if (value != other[x, y]) {
                eq = false
                return@forEachIndexed
            }
        }
        return eq
    }

    override fun hashCode(): Int {
        var h = 17
        h = h * 39 + cols
        h = h * 39 + rows
        forEach { h = h * 37 + it.hashCode() }
        return h
    }
}

internal open class TransposedMatrix(protected val original: Matrix) : AbstractMatrix() {
    override val cols: Int
        get() = original.rows

    override val rows: Int
        get() = original.cols

    override fun get(x: Int, y: Int): MatrixType = original[y, x]
}

internal class TransposedMutableMatrix(original: MutableMatrix) :
        TransposedMatrix(original), MutableMatrix {
    override fun set(x: Int, y: Int, value: MatrixType) {
        (original as MutableMatrix)[y, x] = value
    }
}

fun Matrix.transpose(): Matrix = TransposedMatrix(this)

fun MutableMatrix.transpose(): MutableMatrix = TransposedMutableMatrix(this)

open class ListMatrix(override val cols: Int, override val rows: Int,
                      protected val list: List<MatrixType>) :
        AbstractMatrix() {
    override operator fun get(x: Int, y: Int): MatrixType = list[y * cols + x]
}

internal class MutableListMatrix(cols: Int, rows: Int, list: MutableList<MatrixType>) :
        ListMatrix(cols, rows, list), MutableMatrix {
    override fun set(x: Int, y: Int, value: MatrixType) {
        (list as MutableList<MatrixType>)[y * cols + x] = value
    }
}

fun matrixOf(cols: Int, rows: Int, vararg elements: MatrixType): Matrix {
    return ListMatrix(cols, rows, elements.asList())
}

fun mutableMatrixOf(cols: Int, rows: Int, vararg elements: MatrixType): MutableMatrix {
    return MutableListMatrix(cols, rows, elements.toMutableList())
}

inline private fun prepareListForMatrix(cols: Int, rows: Int, init: (Int, Int) -> MatrixType): ArrayList<MatrixType> {
    val list = ArrayList<MatrixType>(cols * rows)
    for (y in 0..rows - 1) {
        (0..cols - 1).mapTo(list) { init(it, y) }
    }
    return list
}

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
inline fun createMatrix(cols: Int, rows: Int, init: (Int, Int) -> MatrixType): Matrix {
    return ListMatrix(cols, rows, prepareListForMatrix(cols, rows, init))
}

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
inline fun createMutableMatrix(cols: Int, rows: Int, init: (Int, Int) -> MatrixType): MutableMatrix {
    return MutableListMatrix(cols, rows, prepareListForMatrix(cols, rows, init))
}

inline fun Matrix.mapIndexed(transform: (Int, Int, MatrixType) -> MatrixType): Matrix {
    return createMatrix(cols, rows) { x, y -> transform(x, y, this[x, y]) }
}

inline fun Matrix.map(transform: (MatrixType) -> MatrixType): Matrix {
    return mapIndexed { x, y, value -> transform(value) }
}

inline fun Matrix.forEachIndexed(action: (Int, Int, MatrixType) -> Unit): Unit {
    for (y in 0..rows - 1) {
        for (x in 0..cols - 1) {
            action(x, y, this[x, y])
        }
    }
}

inline fun Matrix.forEach(action: (MatrixType) -> Unit): Unit {
    forEachIndexed { x, y, value -> action(value) }
}

fun Matrix.toList(): List<MatrixType> {
    return prepareListForMatrix(cols, rows, { x, y -> this[x, y] })
}

fun Matrix.toMutableList(): MutableList<MatrixType> {
    return prepareListForMatrix(cols, rows, { x, y -> this[x, y] })
}

private fun Iterable<MatrixType>.toArrayList(size: Int): ArrayList<MatrixType> {
    val list = ArrayList<MatrixType>(size)
    val itr = iterator()

    for (i in 0..size - 1) {
        if (itr.hasNext()) {
            list.add(itr.next())
        } else {
            throw IllegalArgumentException("No enough elements")
        }
    }
    return list
}

fun Iterable<MatrixType>.toMatrix(cols: Int, rows: Int): Matrix {
    val list = toArrayList(cols * rows)
    return ListMatrix(cols, rows, list)
}

fun Iterable<MatrixType>.toMutableMatrix(cols: Int, rows: Int): MutableMatrix {
    val list = toArrayList(cols * rows)
    return MutableListMatrix(cols, rows, list)
}


operator fun Matrix.plus(other: Matrix): Matrix {
    if (rows != other.rows || cols != other.cols)
        throw IllegalArgumentException("Matrices not match")

    return mapIndexed { x, y, value -> value + other[x, y] }
}

operator fun Matrix.unaryMinus(): Matrix {
    return map { -it }
}

operator fun Matrix.minus(other: Matrix): Matrix {
    return this + (-other)
}

operator fun Matrix.times(other: Matrix): Matrix {
    if (rows != other.rows || cols != other.cols)
        throw IllegalArgumentException("Matrices not match")

    return mapIndexed { x, y, value -> value * other[x, y] }
}

operator fun Matrix.times(other: Number): Matrix {
    return map { it * other.toDouble() }
}

operator fun Number.times(other: Matrix): Matrix {
    return other * this
}

infix fun Matrix.x(other: Matrix): Matrix {
    if (rows != other.cols)
        throw IllegalArgumentException("Matrices not match")

    return createMatrix(cols, other.rows) { x, y -> (0..rows - 1).sumByDouble { this[x, it] * other[it, y] } }
}

inline fun Matrix.match(rhs: Matrix) = rows == rhs.rows && cols == rhs.cols

fun Matrix.matchCheck(rhs: Matrix) {
    if (!this.match(rhs)) {
        throw IllegalArgumentException("Matrices not match")
    }
}