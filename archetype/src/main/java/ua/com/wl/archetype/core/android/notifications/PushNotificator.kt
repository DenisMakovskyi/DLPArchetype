package ua.com.wl.archetype.core.android.notifications

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.os.Build

import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat

import ua.com.wl.archetype.core.android.notifications.data.*

/**
 * @author Denis Makovskyi
 */

object PushNotificator {

    fun showPushNotification(context: Context, notification: PushNotification) {
        NotificationCompat.Builder(context, notification.channel.channelId).apply {
            // - alarm
            notification.alarm.sound?.let { setSound(it) }
            notification.alarm.vibrate?.let { setVibrate(it) }
            notification.alarm.ledLight?.let { setLights(it.argb, it.onMs, it.offMs) }
            // - icons
            notification.icons.let { icons ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (icons.smallIcon > 0) {
                        setSmallIcon(icons.smallIcon)
                    }
                    if (icons.smallTint > 0) {
                        color = ContextCompat.getColor(context, icons.smallTint)
                    }

                } else {
                    setSmallIcon(icons.smallIcon)
                }
                icons.largeIcon?.let { icon -> setLargeIcon(icon) }
            }
            // - content
            var textStyle: NotificationCompat.BigTextStyle? = null
            notification.content.info?.let { setContentInfo(it) }
            notification.content.title?.let { title ->
                textStyle = NotificationCompat.BigTextStyle().also { style -> style.setBigContentTitle(title) }
                setContentTitle(title)
            } ?: run {
                textStyle = null
            }
            notification.content.message?.let { message ->
                textStyle = NotificationCompat.BigTextStyle().also { style -> style.bigText(message) }
                setContentText(message)
            } ?: run {
                textStyle = null
            }
            textStyle?.let { setStyle(it) }
            // - actions
            notification.content.actions?.let { actions ->
                for (action in actions) {
                    addAction(action)
                }
            }
            // - intention
            setAutoCancel(notification.intention.autoCancel)
            notification.intention.contentIntent?.let { setContentIntent(it) }
            // - identifier
            notification.identifier.groupKey?.let { gKey ->
                setGroup(gKey)
                setGroupSummary(true)
                notification.identifier.sortKey?.let { sKey -> setSortKey(sKey) }
            }
            // - channel
            priority = notification.channel.priority

        }.let { builder ->
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                // - channel and channel group
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // - channel
                    if (getNotificationChannel(notification.channel.channelId) == null) {
                        createNotificationChannel(createChannel(notification.channel, notification.alarm))
                    }
                    // - group
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        notification.channel.groupId?.let { groupId ->
                            if (getNotificationChannelGroup(groupId) == null) {
                                createChannelGroup(notification.channel)?.let { group ->
                                    createNotificationChannelGroup(group)
                                }
                            }
                        }
                    }
                }
            }.let { manager ->
                manager.notify(notification.identifier.notificationId, builder.build())
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel(channel: Channel, alarm: Alarm): NotificationChannel =
        NotificationChannel(channel.channelId, channel.channelName, channel.importance)
            .apply {
                channel.groupId?.let { group = it }
                channel.channelDescription?.let { description = it }
                // - sound
                alarm.sound?.let {
                    setSound(it, AudioAttributes.Builder()
                        .apply {
                            setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                setAllowedCapturePolicy(AudioAttributes.ALLOW_CAPTURE_BY_ALL)
                            }
                        }.build())
                }
                // - vibration
                alarm.vibrate?.let {
                    enableVibration(true)
                    vibrationPattern = it

                } ?: enableVibration(false)
                // - led indicator
                alarm.ledLight?.let {
                    enableLights(true)
                    lightColor = it.argb

                } ?: enableLights(false)
            }

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannelGroup(channel: Channel): NotificationChannelGroup? =
        if (channel.groupId != null && channel.groupName != null) {
            NotificationChannelGroup(channel.groupId, channel.groupName).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    channel.groupDescription?.let { description = it }
                }
            }
        } else {
            null
        }
}