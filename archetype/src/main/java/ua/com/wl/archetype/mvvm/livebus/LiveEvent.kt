package ua.com.wl.archetype.mvvm.livebus

import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

import androidx.lifecycle.Observer
import androidx.lifecycle.MutableLiveData

import ua.com.wl.archetype.utils.concurrentHashMapOf

/**
 * @author Denis Makovskyi
 */

class LiveEvent<T>: MutableLiveData<T>() {

    private val pendingObservers: ConcurrentHashMap<Observer<in T>, Pair<Observer<in T>, AtomicBoolean>> =
        concurrentHashMapOf()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val interceptor: Observer<in T> = object : Observer<T> {
            override fun onChanged(t: T?) {
                var observeToTrigger: Observer<in T>? = null
                synchronized(pendingObservers) {
                    if (pendingObservers.containsKey(this)) {
                        pendingObservers[this]?.let { pair ->
                            if (pair.second.compareAndSet(true, false)) {
                                observeToTrigger = pair.first
                            }
                        }
                    }
                }
                observeToTrigger?.onChanged(t)
            }
        }

        synchronized(pendingObservers) {
            pendingObservers[interceptor] = Pair(observer, AtomicBoolean(false))
        }

        super.observe(owner, interceptor)
    }

    override fun removeObserver(observer: Observer<in T>) {
        super.removeObserver(observer)
        synchronized(pendingObservers) {
            pendingObservers.entries.forEach {
                if (observer == it.key || observer == it.value.first) {
                    pendingObservers.remove(it.key)
                }
            }
        }
    }

    override fun postValue(value: T?) {
        synchronized(pendingObservers) {
            pendingObservers.values.forEach {
                it.second.set(true)
            }
        }
        super.postValue(value)
    }

    override fun setValue(value: T?) {
        synchronized(pendingObservers) {
            pendingObservers.values.forEach {
                it.second.set(true)
            }
        }
        super.setValue(value)
    }

    fun call() {
        value = null
    }
}