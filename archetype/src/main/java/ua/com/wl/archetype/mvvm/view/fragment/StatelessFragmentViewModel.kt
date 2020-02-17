package ua.com.wl.archetype.mvvm.view.fragment

import android.app.Application

import ua.com.wl.archetype.mvvm.BaseViewModel

/**
 * @author Denis Makovskyi
 */

open class StatelessFragmentViewModel(application: Application) : BaseViewModel(application), FragmentLifecycleCallbacks