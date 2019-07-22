@file:Suppress("ArrayInDataClass")

package ua.com.wl.archetype.core.android.notifications.data

import android.content.Context

import ua.com.wl.archetype.core.android.notifications.PushNotificator
import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker

/**
 * @author Denis Makovskyi
 */

data class PushNotification(
    val alarm: Alarm,
    val icons: Icons,
    val content: Content,
    val channel: Channel,
    val intention: Intention,
    val identifier: Identifier) {

    @NotificationMarker
    class Builder(
        var alarm: Alarm = Alarm.Builder().make(),
        var icons: Icons = Icons.Builder().make(),
        var content: Content = Content.Builder().make(),
        var channel: Channel = Channel.Builder().make(),
        var intention: Intention = Intention.Builder().make(),
        var identifier: Identifier = Identifier.Builder().make()) {

        fun build(init: Builder.() -> Unit): PushNotification {
            init()
            return PushNotification(alarm, icons, content, channel, intention, identifier)
        }
    }

    fun show(context: Context) = PushNotificator.showPushNotification(context, this)
}

fun notification(init: PushNotification.Builder.() -> Unit): PushNotification = PushNotification.Builder().build(init)

