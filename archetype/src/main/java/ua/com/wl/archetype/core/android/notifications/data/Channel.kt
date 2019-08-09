package ua.com.wl.archetype.core.android.notifications.data

import android.app.NotificationManager

import androidx.core.app.NotificationCompat

import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker

/**
 * @author Denis Makovskyi
 */

data class Channel(
    val priority: Int,
    val importance: Int,
    val channelId: String,
    val channelName: String,
    val channelDescription: String?,
    val groupId: String?,
    val groupName: String?,
    val groupDescription: String?) {

    @NotificationMarker
    class Builder(
        var priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        var importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        var channelId: String = "CHANNEL_GENERAL",
        var channelName: String = "GENERAL CHANNEL",
        var channelDescription: String? = null,
        var groupId: String? = null,
        var groupName: String? = null,
        var groupDescription: String? = null) {

        fun build(init: Builder.() -> Unit): Channel {
            init()
            return make()
        }

        internal fun make(): Channel = Channel(
            priority, importance,
            channelId, channelName, channelDescription,
            groupId, groupName, groupDescription)
    }
}

fun PushNotification.Builder.channel(init: Channel.Builder.() -> Unit) {
    channel = Channel.Builder().build(init)
}

fun channel(init: Channel.Builder.() -> Unit): Channel = Channel.Builder().build(init)