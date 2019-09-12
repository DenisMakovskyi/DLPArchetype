package ua.com.wl.archetype.core.android.notifications.data

import android.content.Intent

/**
 * @author Denis Makovskyi
 */

data class TaskStackElement(val intent: Intent, val howPut: HowPut? = null) {

    enum class HowPut {
        ONLY_NEXT_INTENT,
        ONLY_EXTRACT_PARENT,
        NEXT_INTENT_WITH_PARENT
    }
}