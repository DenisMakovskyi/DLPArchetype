package ua.com.wl.archetype.core.android.notifications.helpers

import kotlin.properties.Delegates

import android.app.PendingIntent
import android.content.Context

import androidx.core.app.TaskStackBuilder

import ua.com.wl.archetype.core.android.notifications.data.Intention
import ua.com.wl.archetype.core.android.notifications.data.TaskStackElement
import ua.com.wl.archetype.core.android.notifications.dsl.PendingIntentMarker

/**
 * @author Denis Makovskyi
 */

@PendingIntentMarker
class PendingIntentBuilder {

    enum class From {
        SERVICE,
        ACTIVITY
    }

    var from: From = From.ACTIVITY
    var context: Context by Delegates.notNull()
    var requestCode: Int = 100
    var singleTarget: Boolean = true
    var taskStackElements: List<TaskStackElement> = listOf()
    var pendingIntentFlags: Int = PendingIntent.FLAG_UPDATE_CURRENT

    fun build(init: PendingIntentBuilder.() -> Unit): PendingIntent {
        init()
        return when(from) {
            From.SERVICE -> {
                require(taskStackElements.isNotEmpty()) {
                    "Can not create service pending intent from empty task elements list"
                }
                val target = taskStackElements.first().intent
                PendingIntent.getService(context, requestCode, target, pendingIntentFlags)
            }
            From.ACTIVITY -> {
                require(taskStackElements.isNotEmpty()) {
                    "Can not create activity pending intent from empty task elements list"
                }
                if (singleTarget) {
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

fun Intention.Builder.pendingIntent(init: PendingIntentBuilder.() -> Unit) {
    contentIntent = PendingIntentBuilder().build(init)
}

fun pendingIntent(init: PendingIntentBuilder.() -> Unit): PendingIntent = PendingIntentBuilder().build(init)