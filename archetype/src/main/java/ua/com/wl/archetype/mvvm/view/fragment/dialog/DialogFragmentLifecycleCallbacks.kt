package ua.com.wl.archetype.mvvm.view.fragment.dialog

import android.os.Bundle
import android.content.Intent

import androidx.lifecycle.LifecycleObserver

/**
 * @author Denis Makovskyi
 */

interface DialogFragmentLifecycleCallbacks : LifecycleObserver {

    fun onViewCreated() {}

    fun onActivityCreated() {}

    fun onViewStateRestored(state: Bundle?) {}

    fun onStart() {}

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    fun onResume() {}

    fun onPause() {}

    fun onSaveInstanceState(state: Bundle) {}

    fun onStop() {}

    fun onDestroyView() {}

    fun onDestroy() {}

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}
}