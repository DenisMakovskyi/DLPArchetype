package ua.com.wl.archetype.mvvm.view.fragment.dialog

import android.app.Application

import androidx.lifecycle.SavedStateHandle
import ua.com.wl.archetype.mvvm.BaseViewModel

open class StatefulDialogFragmentViewModel(
    application: Application,
    protected val savedStateHandle: SavedStateHandle
): BaseViewModel(application), DialogFragmentLifecycleCallbacks