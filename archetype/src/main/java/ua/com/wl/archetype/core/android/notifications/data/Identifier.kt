package ua.com.wl.archetype.core.android.notifications.data

import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker

/**
 * @author Denis Makovskyi
 */

data class Identifier(
    val notificationId: Int,
    val groupKey: String?,
    val sortKey: String?) {

    @NotificationMarker
    class Builder(
        var notificationId: Int = 0,
        var groupKey: String? = null,
        var sortKey: String? = null) {

        fun build(init: Builder.() -> Unit): Identifier {
            init()
            return make()
        }

        internal fun make(): Identifier = Identifier(notificationId, groupKey, sortKey)
    }
}

fun PushNotification.Builder.identifier(init: Identifier.Builder.() -> Unit) {
    identifier = Identifier.Builder().build(init)
}

fun identifier(init: Identifier.Builder.() -> Unit): Identifier = Identifier.Builder().build(init)