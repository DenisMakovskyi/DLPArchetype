package ua.com.wl.archetype.core.android.notifications.data

import android.app.Activity
import kotlin.properties.Delegates

import android.content.Intent
import android.content.Context
import android.app.PendingIntent
import android.app.Service

import ua.com.wl.archetype.core.android.notifications.dsl.IntentionMarker
import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker
import ua.com.wl.archetype.utils.isJClassAssignableFrom

data class Intention(
    val autoCancel: Boolean,
    val contentIntent: PendingIntent?) {

    @IntentionMarker
    @NotificationMarker
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

@IntentionMarker
class PendingIntentBuilder {

    enum class From {
        Service,
        Activity
    }

    var from: From = From.Service
    var context: Context by Delegates.notNull()
    var requestCode: Int = 100
    var intentClass: Class<*> by Delegates.notNull()
    var intentFlags: Int = Intent.FLAG_ACTIVITY_NEW_TASK
    val intentExtras: Map<String, Any>? = null
    var intentCategories: List<String>? = null
    val pendingIntentFlags: Int = PendingIntent.FLAG_UPDATE_CURRENT

    fun build(init: PendingIntentBuilder.() -> Unit): PendingIntent {
        init()
        if (!isJClassAssignableFrom(
                intentClass,
                Service::class.java, Activity::class.java)) {
            throw IllegalArgumentException("Intent class must be assignable either Service or Activity")
        }
        val intent = Intent(context, intentClass).apply {
            addFlags(intentFlags)
            intentExtras?.let { extras ->
                for (extra in extras) {
                    when (extra.value) {
                        is Boolean -> putExtra(extra.key, extra.value as Boolean)
                        is Byte -> putExtra(extra.key, extra.value as Byte)
                        is Char -> putExtra(extra.key, extra.value as Char)
                        is Int -> putExtra(extra.key, extra.value as Int)
                        is Long -> putExtra(extra.key, extra.value as Long)
                        is Float -> putExtra(extra.key, extra.value as Float)
                        is Double -> putExtra(extra.key, extra.value as Double)
                        is String -> putExtra(extra.key, extra.value as String)
                    }
                }
            }
            intentCategories?.let { categories ->
                for (category in categories) {
                    addCategory(category)
                }
            }
        }
        return when (from) {
            From.Service -> PendingIntent.getService(context, requestCode, intent, pendingIntentFlags)
            From.Activity -> PendingIntent.getActivity(context, requestCode, intent, pendingIntentFlags)
        }
    }
}

fun PushNotification.Builder.intention(init: Intention.Builder.() -> Unit) {
    intention = Intention.Builder().build(init)
}

fun Intention.Builder.pendingIntent(init: PendingIntentBuilder.() -> Unit) {
    contentIntent = PendingIntentBuilder().build(init)
}

fun intention(init: Intention.Builder.() -> Unit): Intention = Intention.Builder().build(init)

fun pendingIntent(init: PendingIntentBuilder.() -> Unit): PendingIntent = PendingIntentBuilder().build(init)