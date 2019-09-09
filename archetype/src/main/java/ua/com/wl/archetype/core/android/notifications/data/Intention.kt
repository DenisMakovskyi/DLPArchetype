package ua.com.wl.archetype.core.android.notifications.data

import kotlin.properties.Delegates

import android.content.Intent
import android.content.Context
import android.app.PendingIntent

import androidx.core.app.TaskStackBuilder

import ua.com.wl.archetype.core.android.notifications.dsl.IntentionMarker
import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker
import ua.com.wl.archetype.utils.toBundle

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
    var intentClass: Class<*>? = null
    var parentStack: List<Class<*>>? = null
    var intentAction: String? = null
    var intentFlags: List<Int>? = null
    var intentCategories: List<String>? = null
    var intentExtras: Map<String, Any?>? = null
    var pendingIntentFlags: Int = PendingIntent.FLAG_UPDATE_CURRENT

    fun build(init: PendingIntentBuilder.() -> Unit): PendingIntent {
        init()
        val intent = if (intentClass == null) Intent() else Intent(context, intentClass)
        intent.apply {
            intentAction?.let {
                action = it
            }
            intentFlags?.let { flags ->
                for (flag in flags) {
                    addFlags(flag)
                }
            }
            intentCategories?.let { categories ->
                for (category in categories) {
                    addCategory(category)
                }
            }
            intentExtras?.let { extras ->
                putExtras(extras.toBundle())
            }
        }
        return when (from) {
            From.Service -> PendingIntent.getService(context, requestCode, intent, pendingIntentFlags)
            From.Activity -> {
                parentStack?.let { stack ->
                    TaskStackBuilder.create(context).run {
                        for (item in stack) {
                            addParentStack(item)
                        }
                        addNextIntentWithParentStack(intent)
                        getPendingIntent(requestCode, pendingIntentFlags)
                    }

                } ?: run {
                    PendingIntent.getActivity(context, requestCode, intent, pendingIntentFlags)
                }
            }
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