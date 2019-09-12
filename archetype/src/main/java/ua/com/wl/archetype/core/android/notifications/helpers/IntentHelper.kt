package ua.com.wl.archetype.core.android.notifications.helpers

import kotlin.properties.Delegates

import android.content.Context
import android.content.Intent

import ua.com.wl.archetype.utils.toBundle

/**
 * @author Denis Makovskyi
 */

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

fun intent(init: IntentBuilder.() -> Unit): Intent = IntentBuilder().build(init)