package ua.com.wl.archetype.mvvm.view.activity

import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.lifecycle.LifecycleObserver

import io.reactivex.disposables.Disposable
import io.reactivex.disposables.CompositeDisposable

import ua.com.wl.archetype.mvvm.livebus.LiveBus
import ua.com.wl.archetype.mvvm.ObservableViewModel

/**
 * @author Denis Makovskyi
 */

abstract class ActivityViewModel(application: Application) : ObservableViewModel(application), LifecycleObserver {

    val liveBus = LiveBus()
    private val compositeDisposable = CompositeDisposable()

    open fun onStart() {}

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    open fun onRestoreInstanceState(state: Bundle) {}

    open fun onPostCreate(state: Bundle?) {}

    open fun onResume() {}

    open fun onCreateOptionsMenu(menu: Menu) {}

    open fun onPrepareOptionsMenu(menu: Menu) {}

    open fun onPause() {}

    open fun onSaveInstanceState(state: Bundle) {}

    open fun onStop() {}

    open fun onConfigurationChanged(newConfig: Configuration) {}

    open fun onDestroy() {}

    open fun onWindowFocusChanged(hasFocus: Boolean) {}

    open fun onOptionsItemSelected(item: MenuItem) {}

    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}

    open fun onBackPressed(): Boolean = false

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun deleteDisposable(disposable: Disposable) {
        compositeDisposable.delete(disposable)
    }

    protected fun clearDisposables() = compositeDisposable.clear()
}