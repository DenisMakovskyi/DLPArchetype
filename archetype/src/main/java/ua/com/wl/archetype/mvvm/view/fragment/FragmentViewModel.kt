package ua.com.wl.archetype.mvvm.view.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem

import androidx.lifecycle.LifecycleObserver

import io.reactivex.disposables.Disposable
import io.reactivex.disposables.CompositeDisposable

import ua.com.wl.archetype.mvvm.livebus.LiveBus
import ua.com.wl.archetype.mvvm.ObservableViewModel

/**
 * @author Denis Makovskyi
 */

open class FragmentViewModel(application: Application) : ObservableViewModel(application), LifecycleObserver {

    val liveBus = LiveBus()
    private val compositeDisposable = CompositeDisposable()

    open fun onViewCreated() {}

    open fun onActivityCreated() {}

    open fun onViewStateRestored(state: Bundle?) {}

    open fun onStart() {}

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    open fun onResume() {}

    open fun onCreateOptionsMenu() {}

    open fun onPrepareOptionsMenu() {}

    open fun onPause() {}

    open fun onSaveInstanceState(state: Bundle) {}

    open fun onStop() {}

    open fun onDestroyView() {}

    open fun onDestroy() {}

    open fun onOptionsItemSelected(item: MenuItem) {}

    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun deleteDisposable(disposable: Disposable) {
        compositeDisposable.delete(disposable)
    }

    protected fun clearDisposables() = compositeDisposable.clear()
}