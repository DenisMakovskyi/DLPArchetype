package ua.com.wl.archetype.utils

import android.os.Bundle
import android.os.Parcelable

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

fun <V> Map<String, V?>.toBundle(): Bundle {
    val bundle = Bundle(size)
    for (entry in this) {
        when (val value = entry.value) {
            null -> bundle.putString(entry.key, null)

            // Scalars
            is Boolean -> bundle.putBoolean(entry.key, value)
            is Byte -> bundle.putByte(entry.key, value)
            is Char -> bundle.putChar(entry.key, value)
            is Double -> bundle.putDouble(entry.key, value)
            is Float -> bundle.putFloat(entry.key, value)
            is Int -> bundle.putInt(entry.key, value)
            is Long -> bundle.putLong(entry.key, value)
            is Short -> bundle.putShort(entry.key, value)

            // References
            is Bundle -> bundle.putBundle(entry.key, value)
            is CharSequence -> bundle.putCharSequence(entry.key, value)
            is Parcelable -> bundle.putParcelable(entry.key, value)

            // Scalar arrays
            is BooleanArray -> bundle.putBooleanArray(entry.key, value)
            is ByteArray -> bundle.putByteArray(entry.key, value)
            is CharArray -> bundle.putCharArray(entry.key, value)
            is DoubleArray -> bundle.putDoubleArray(entry.key, value)
            is FloatArray -> bundle.putFloatArray(entry.key, value)
            is IntArray -> bundle.putIntArray(entry.key, value)
            is LongArray -> bundle.putLongArray(entry.key, value)
            is ShortArray -> bundle.putShortArray(entry.key, value)
        }
    }
    return bundle
}