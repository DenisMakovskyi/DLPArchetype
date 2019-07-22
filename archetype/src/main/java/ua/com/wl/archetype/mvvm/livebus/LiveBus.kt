@file:Suppress("UNCHECKED_CAST")

package ua.com.wl.archetype.mvvm.livebus

import androidx.lifecycle.Observer
import androidx.lifecycle.LifecycleOwner

import ua.com.wl.archetype.utils.getOrElse

/**
 * @author Denis Makovskyi
 */

class LiveBus {

    private val eventMap: MutableMap<Class<out Event>, LiveEvent<out Event>> = hashMapOf()

    fun <T : Event> observe(lifecycleOwner: LifecycleOwner, eventClass: Class<T>, observer: Observer<T>) {
        val liveEvent = eventMap.getOrElse(eventClass, initLiveEvent(eventClass)) as LiveEvent<T>
        liveEvent.observe(lifecycleOwner, observer)
    }

    fun <T : Event> send(event: T) {
        eventMap[event::class.java]?.value = event
    }

    private fun <T : Event> initLiveEvent(eventClass: Class<T>): LiveEvent<T> {
        val liveEvent: LiveEvent<T> = LiveEvent()
        eventMap[eventClass] = liveEvent
        return liveEvent
    }
}