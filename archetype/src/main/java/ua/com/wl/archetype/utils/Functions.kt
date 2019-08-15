package ua.com.wl.archetype.utils

import java.util.concurrent.ConcurrentHashMap

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * @author Denis Makovskyi
 */

fun <K, V> concurrentHashMapOf(): ConcurrentHashMap<K, V> = ConcurrentHashMap()

fun ioScope() = CoroutineScope(Dispatchers.IO + SupervisorJob())

fun mainScope() = CoroutineScope(Dispatchers.Main + SupervisorJob())

fun defaultScope() = CoroutineScope(Dispatchers.Default + SupervisorJob())