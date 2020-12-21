package ua.com.wl.archetype.mvvm.view.fragment

import android.app.Application

import androidx.lifecycle.SavedStateHandle

import ua.com.wl.archetype.mvvm.BaseViewModel

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
open class StatefulFragmentViewModel(
    application: Application,
    protected val savedStateHandle: SavedStateHandle
) : BaseViewModel(application), FragmentLifecycleCallbacks