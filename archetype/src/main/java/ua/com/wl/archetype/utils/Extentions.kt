package ua.com.wl.archetype.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author Denis Makovskyi
 */

fun String.Companion.empty() = ""

inline fun <T> Iterable<T>.has(predicate: (T) -> Boolean): Boolean {
    for (element in this) if (predicate(element)) return true
    return false
}

inline fun <T> Iterable<T>.whenIndex(index: Int, block: () -> Unit) {
    if (index > -1 && index < this.count()) block()
}

fun <K, V> Map<K, V>.getOrElse(key: K, defaultValue: V): V = this[key] ?: defaultValue

fun <K, V> Map<K, V>.toArray(): Array<Pair<K, V>> {
    val array: Array<Pair<K, V>> = arrayOf()
    var i = 0
    for (entry in this) {
        array[i] = Pair(entry.key, entry.value)
        i += 1
    }
    return array
}

internal fun RecyclerView.LayoutManager.findLastVisibleItemPosition(): Int {
    return when (this) {
        is GridLayoutManager -> findLastVisibleItemPosition()
        is LinearLayoutManager -> findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val lastVisibleItemPositions = findLastVisibleItemPositions(null)
            var maxSize = 0
            for (i in lastVisibleItemPositions.indices) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i]

                } else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i]
                }
            }
            return maxSize
        }
        else -> throw IllegalStateException("Recycler view layout manager is not supported")
    }
}