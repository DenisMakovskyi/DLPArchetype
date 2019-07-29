package ua.com.wl.archetype.core.android.bus

import org.greenrobot.eventbus.EventBus

object Bus {

    var eventsBus: EventBus? = null
        get() {
            if (field == null) {
                synchronized(this) {
                    if (field == null) {
                        field = EventBus.getDefault()
                    }
                }
            }
            return field
        }
}