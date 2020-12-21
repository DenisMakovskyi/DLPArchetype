package ua.com.wl.archetype.mvvm

import android.app.Application

import io.reactivex.disposables.Disposable
import io.reactivex.disposables.CompositeDisposable

import ua.com.wl.archetype.mvvm.livebus.LiveBus

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
open class BaseViewModel(application: Application): ObservableViewModel(application) {

    val liveBus = LiveBus()
    private val disposables = CompositeDisposable()

    fun getApp(): Application {
        return getApplication()
    }

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    protected fun deleteDisposable(disposable: Disposable) {
        disposables.delete(disposable)
    }

    protected fun clearDisposables() {
        disposables.clear()
    }
}