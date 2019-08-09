package ua.com.wl.archetype.utils

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Denis Makovskyi
 */

internal fun <K, V> concurrentHashMapOf(): ConcurrentHashMap<K, V> = ConcurrentHashMap()

internal fun String.Companion.empty() = ""

internal inline fun <T> Iterable<T>.has(predicate: (T) -> Boolean): Boolean {
    for (element in this) if (predicate(element)) return true
    return false
}

internal inline fun <T> Iterable<T>.whenIndex(index: Int, block: () -> Unit) {
    if (index > -1 && index < this.count()) block()
}

internal fun <K, V> Map<K, V>.getOrElse(key: K, defaultValue: V): V = this[key] ?: defaultValue

internal fun <K, V> Map<K, V>.toArray(): Array<Pair<K, V>> {
    val array: Array<Pair<K, V>> = arrayOf()
    var i = 0
    for (entry in this) {
        array[i] = Pair(entry.key, entry.value)
        i += 1
    }
    return array
}