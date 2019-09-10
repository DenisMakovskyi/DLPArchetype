package ua.com.wl.archetype.core.android.notifications.data

import kotlin.properties.Delegates

import android.content.Intent
import android.content.Context
import android.app.PendingIntent
import androidx.core.app.TaskStackBuilder


import ua.com.wl.archetype.core.android.notifications.dsl.PendingIntentMarker
import ua.com.wl.archetype.core.android.notifications.dsl.NotificationMarker
import ua.com.wl.archetype.utils.toBundle

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

@PendingIntentMarker
class PendingIntentBuilder {

    enum class From {
        Service,
        Activity
    }

    var from: From = From.Activity
    var context: Context by Delegates.notNull()
    var requestCode: Int = 100
    var singleTarget: Boolean = true
    var taskStackElements: List<TaskStackElement> = listOf()
    var pendingIntentFlags: Int = PendingIntent.FLAG_UPDATE_CURRENT

    fun build(init: PendingIntentBuilder.() -> Unit): PendingIntent {
        init()
        return when(from) {
            From.Service -> {
                require(taskStackElements.isNotEmpty()) {
                    "Can not create service pending intent from empty task elements list"
                }
                val target = taskStackElements.first().intent
                PendingIntent.getService(context, requestCode, target, pendingIntentFlags)
            }
            From.Activity -> {
                if (singleTarget) {
                    require(taskStackElements.isNotEmpty()) {
                        "Can not create activity pending intent from empty task elements list"
                    }
                    val target = taskStackElements.first().intent
                    PendingIntent.getActivity(context, requestCode, target, pendingIntentFlags)

                } else {
                    requireNotNull(TaskStackBuilder.create(context).run {
                        for (element in taskStackElements) {
                            when(element.howPut) {
                                TaskStackElement.HowPut.ONLY_NEXT_INTENT -> addNextIntent(element.intent)
                                TaskStackElement.HowPut.ONLY_EXTRACT_PARENT -> addParentStack(element.intent.component)
                                TaskStackElement.HowPut.NEXT_INTENT_WITH_PARENT -> addNextIntentWithParentStack(element.intent)
                            }
                        }
                        getPendingIntent(requestCode, pendingIntentFlags)
                    })
                }
            }
        }
    }
}

class IntentBuilder {

    var context: Context by Delegates.notNull()
    var intentClass: Class<*> by Delegates.notNull()
    var intentAction: String? = null
    var intentFlags: List<Int>? = null
    var intentCategories: List<String>? = null
    var intentExtras: Map<String, Any?>? = null

    fun build(init: IntentBuilder.() -> Unit): Intent {
        init()
        return Intent(context, intentClass).apply {
            intentAction?.let { it ->
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
    }
}

data class TaskStackElement(val intent: Intent, val howPut: HowPut) {

    enum class HowPut {
        ONLY_NEXT_INTENT,
        ONLY_EXTRACT_PARENT,
        NEXT_INTENT_WITH_PARENT
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

fun intent(init: IntentBuilder.() -> Unit): Intent = IntentBuilder().build(init)