package ua.com.wl.archetype.utils

import androidx.annotation.RestrictTo

/**
 * @author Denis Makovskyi
 */

@RestrictTo(RestrictTo.Scope.LIBRARY)
inline fun <T> Iterable<T>.has(predicate: (T) -> Boolean): Boolean {
    for (element in this) if (predicate(element)) return true
    return false
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun <K, V> Map<K, V>.getOrElse(key: K, defaultValue: V): V {
    return this[key] ?: defaultValue
}