@file:Suppress("ArrayInDataClass")

package ua.com.wl.archetype.core.android.notifications.data

import java.util.concurrent.TimeUnit

import android.net.Uri
import android.graphics.Color

import androidx.annotation.IntRange
import androidx.core.app.NotificationCompat

import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker

/**
 * @author Denis Makovskyi
 */

data class LEDLight(
    val argb: Int = Color.BLUE,
    val onMs: Int = TimeUnit.SECONDS.toMillis(1).toInt(),
    val offMs: Int = TimeUnit.SECONDS.toMillis(1).toInt())

data class Alarm(
    @IntRange(from = -2, to = 2) val priority: Int,
    val sound: Uri?,
    val vibrate: LongArray?,
    val ledLight: LEDLight?) {

    @NotificationMarker
    class Builder(
        @IntRange(from = -2, to = 2) var priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        var sound: Uri? = null,
        var vibrate: LongArray? = null,
        var ledLight: LEDLight? = null) {

        fun build(init: Builder.() -> Unit): Alarm {
            init()
            return make()
        }

        internal fun make(): Alarm = Alarm(priority, sound, vibrate, ledLight)
    }
}

fun PushNotification.Builder.alarm(init: Alarm.Builder.() -> Unit) {
    alarm = Alarm.Builder().build(init)
}

fun alarm(init: Alarm.Builder.() -> Unit): Alarm = Alarm.Builder().build(init)