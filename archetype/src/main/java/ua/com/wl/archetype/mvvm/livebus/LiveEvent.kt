package ua.com.wl.archetype.mvvm.livebus

import java.util.concurrent.atomic.AtomicBoolean

import androidx.lifecycle.Observer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

import ua.com.wl.archetype.utils.concurrentHashMapOf

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
class LiveEvent<T>: MutableLiveData<T>() {

    private val pendingObservers = concurrentHashMapOf<Observer<in T>, Pair<Observer<in T>, AtomicBoolean>>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val interceptor = object : Observer<T> {
            override fun onChanged(t: T?) {
                var observeToTrigger: Observer<in T>? = null
                synchronized(pendingObservers) {
                    pendingObservers[this]?.let { pair ->
                        if (pair.second.compareAndSet(true, false)) {
                            observeToTrigger = pair.first
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

    override fun setValue(value: T?) {
        synchronized(pendingObservers) {
            pendingObservers.values.forEach {
                it.second.set(true)
            }
        }
        super.setValue(value)
    }

    override fun postValue(value: T?) {
        synchronized(pendingObservers) {
            pendingObservers.values.forEach {
                it.second.set(true)
            }
        }
        super.postValue(value)
    }

    fun call() {
        value = null
    }
}