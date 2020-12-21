package ua.com.wl.archetype.mvvm

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
open class ObservableViewModel(application: Application) : AndroidViewModel(application), Observable {

    @Transient
    private var callbacks: PropertyChangeRegistry? = null

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (callbacks == null) {
                callbacks = PropertyChangeRegistry()
            }
        }
        callbacks?.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks?.remove(callback)
    }

    fun notifyChange() {
        synchronized(this) {
            callbacks?.notifyCallbacks(this, 0, null)
        }
    }

    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            callbacks?.notifyCallbacks(this, fieldId, null)
        }
    }
}