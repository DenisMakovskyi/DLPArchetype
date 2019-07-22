package ua.com.wl.archetype.core.android.notifications.data

import androidx.core.app.NotificationCompat

import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker

/**
 * @author Denis Makovskyi
 */

data class Content(
    val info: String?,
    val title: String?,
    val message: String?,
    val actions: List<NotificationCompat.Action>?) {

    @NotificationMarker
    class Builder(
        var info: String? = null,
        var title: String? = null,
        var message: String? = null,
        var actions: List<NotificationCompat.Action>? = null) {

        fun build(init: Builder.() -> Unit): Content {
            init()
            return make()
        }

        internal fun make(): Content = Content(info, title, message, actions)
    }
}

fun PushNotification.Builder.content(init: Content.Builder.() -> Unit) {
    content = Content.Builder().build(init)
}

fun content(init: Content.Builder.() -> Unit): Content = Content.Builder().build(init)