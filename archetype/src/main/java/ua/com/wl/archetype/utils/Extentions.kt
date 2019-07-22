package ua.com.wl.archetype.utils

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Denis Makovskyi
 */

internal fun isJClassAssignableFrom(jCls: Class<*>, vararg jClasses: Class<*>): Boolean {
    for (cls in jClasses) {
        if (jCls.isAssignableFrom(cls)) {
            return true
        }
    }
    return false
}

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