package ua.com.wl.archetype.mvvm.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import android.content.res.Configuration

import androidx.lifecycle.LifecycleObserver

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
interface ActivityLifecycleCallbacks : LifecycleObserver {

    fun onStart() {}

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    fun onRestoreInstanceState(state: Bundle) {}

    fun onPostCreate(state: Bundle?) {}

    fun onResume() {}

    fun onCreateOptionsMenu(menu: Menu) {}

    fun onPrepareOptionsMenu(menu: Menu) {}

    fun onPause() {}

    fun onSaveInstanceState(state: Bundle) {}

    fun onStop() {}

    fun onConfigurationChanged(newConfig: Configuration) {}

    fun onDestroy() {}

    fun onWindowFocusChanged(hasFocus: Boolean) {}

    fun onOptionsItemSelected(item: MenuItem) {}

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}

    fun onBackPressed(): Boolean = false
}