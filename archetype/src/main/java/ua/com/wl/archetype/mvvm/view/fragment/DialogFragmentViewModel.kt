package ua.com.wl.archetype.mvvm.view.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle

import androidx.lifecycle.LifecycleObserver

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

import ua.com.wl.archetype.mvvm.ObservableViewModel
import ua.com.wl.archetype.mvvm.livebus.LiveBus

/**
 * @author Denis Makovskyi
 */

abstract class DialogFragmentViewModel(application: Application) : ObservableViewModel(application), LifecycleObserver {

    val liveBus = LiveBus()
    private val compositeDisposable = CompositeDisposable()

    open fun onViewCreated() {}

    open fun onActivityCreated() {}

    open fun onViewStateRestored(state: Bundle?) {}

    open fun onStart() {}

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    open fun onResume() {}

    open fun onPause() {}

    open fun onSaveInstanceState(state: Bundle) {}

    open fun onStop() {}

    open fun onDestroyView() {}

    open fun onDestroy() {}

    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun deleteDisposable(disposable: Disposable) {
        compositeDisposable.delete(disposable)
    }

    protected fun clearDisposables() = compositeDisposable.clear()
}