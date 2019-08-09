package ua.com.wl.archetype.core.android.notifications.data

import android.graphics.Bitmap
import android.app.Notification

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

import androidx.annotation.IntRange

import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker

/**
 * @author Denis Makovskyi
 */

data class Icons(
    @DrawableRes val smallIcon: Int,
    @ColorRes val smallTint: Int,
    val largeIcon: Bitmap?,
    @IntRange(from = 0, to = 2) val badgeIconType: Int) {

    @NotificationMarker
    class Builder(
        @DrawableRes var smallIcon: Int = 0,
        @ColorRes var smallTint: Int = 0,
        var largeIcon: Bitmap? = null,
        @IntRange(from = 0, to = 2) var badgeIconType: Int = Notification.BADGE_ICON_NONE) {

        fun build(init: Builder.() -> Unit): Icons {
            init()
            return make()
        }

        internal fun make(): Icons = Icons(smallIcon, smallTint, largeIcon, badgeIconType)
    }
}

fun PushNotification.Builder.icons(init: Icons.Builder.() -> Unit) {
    icons = Icons.Builder().build(init)
}

fun icons(init: Icons.Builder.() -> Unit): Icons = Icons.Builder().build(init)