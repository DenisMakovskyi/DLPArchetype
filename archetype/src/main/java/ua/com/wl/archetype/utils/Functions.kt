package ua.com.wl.archetype.utils

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Denis Makovskyi
 */

internal fun <K, V> concurrentHashMapOf(): ConcurrentHashMap<K, V> = ConcurrentHashMap()