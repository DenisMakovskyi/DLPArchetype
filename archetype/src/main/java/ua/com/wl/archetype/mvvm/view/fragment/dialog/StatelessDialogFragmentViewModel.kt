package ua.com.wl.archetype.mvvm.view.fragment.dialog

import android.app.Application

import ua.com.wl.archetype.mvvm.BaseViewModel

/**
 * @author Denis Makovskyi
 */

open class StatelessDialogFragmentViewModel(application: Application) :
    BaseViewModel(application), DialogFragmentLifecycleCallbacks