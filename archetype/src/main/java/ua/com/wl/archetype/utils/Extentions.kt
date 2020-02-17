package ua.com.wl.archetype.utils

/**
 * @author Denis Makovskyi
 */

internal fun <T> Iterable<T>.has(predicate: (T) -> Boolean): Boolean {
    for (element in this) if (predicate(element)) return true
    return false
}

internal fun <K, V> Map<K, V>.getOrElse(key: K, defaultValue: V): V {
    return this[key] ?: defaultValue
}