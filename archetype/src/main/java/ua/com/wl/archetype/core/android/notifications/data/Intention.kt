package ua.com.wl.archetype.core.android.notifications.data

import android.app.PendingIntent

import ua.com.wl.archetype.core.android.notifications.dsl.PendingIntentMarker
import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker

/**
 * @author Denis Makovskyi
 */

data class Intention(
    val autoCancel: Boolean,
    val contentIntent: PendingIntent?) {

    @NotificationMarker
    @PendingIntentMarker
    class Builder(
        var autoCancel: Boolean = true,
        var contentIntent: PendingIntent? = null) {

        fun build(init: Builder.() -> Unit): Intention {
            init()
            return make()
        }

        internal fun make(): Intention = Intention(autoCancel, contentIntent)
    }
}

fun PushNotification.Builder.intention(init: Intention.Builder.() -> Unit) {
    intention = Intention.Builder().build(init)
}

fun intention(init: Intention.Builder.() -> Unit): Intention = Intention.Builder().build(init)