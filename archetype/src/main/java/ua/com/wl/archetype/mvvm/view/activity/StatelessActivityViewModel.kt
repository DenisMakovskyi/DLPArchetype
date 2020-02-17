package ua.com.wl.archetype.mvvm.view.activity

import android.app.Application

import ua.com.wl.archetype.mvvm.BaseViewModel

/**
 * @author Denis Makovskyi
 */

open class StatelessActivityViewModel(application: Application) : BaseViewModel(application), ActivityLifecycleCallbacks