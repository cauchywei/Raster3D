/**
 * Created by cauchywei on 2017/6/8.
 */

inline fun <T> MutableList<T>.mapInPlace(mutator: (T) -> T): MutableList<T> {
    val iterate = this.listIterator()
    while (iterate.hasNext()) {
        val oldValue = iterate.next()
        val newValue = mutator(oldValue)
        if (newValue !== oldValue) {
            iterate.set(newValue)
        }
    }
    return this
}

inline fun <T> Array<T>.mapInPlace(mutator: (T) -> T): Array<T> {
    this.forEachIndexed { idx, value ->
        mutator(value).let { newValue ->
            if (newValue !== value) this[idx] = mutator(value)
        }
    }
    return this
}

inline fun BooleanArray.mapInPlace(mutator: (Boolean) -> Boolean): BooleanArray {
    this.forEachIndexed { idx, value ->
        mutator(value).let { newValue ->
            if (newValue != value) this[idx] = mutator(value)
        }
    }
    return this
}