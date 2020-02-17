package ua.com.wl.archetype.mvvm.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent

import androidx.lifecycle.LifecycleObserver

/**
 * @author Denis Makovskyi
 */

interface FragmentLifecycleCallbacks : LifecycleObserver {

    fun onViewCreated() {}

    fun onActivityCreated() {}

    fun onViewStateRestored(state: Bundle?) {}

    fun onStart() {}

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    fun onResume() {}

    fun onCreateOptionsMenu(menu: Menu) {}

    fun onPrepareOptionsMenu(menu: Menu) {}

    fun onPause() {}

    fun onSaveInstanceState(state: Bundle) {}

    fun onStop() {}

    fun onDestroyView() {}

    fun onDestroy() {}

    fun onOptionsItemSelected(item: MenuItem) {}

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}
}