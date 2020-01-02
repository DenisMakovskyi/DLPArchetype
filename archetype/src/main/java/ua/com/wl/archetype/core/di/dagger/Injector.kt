package ua.com.wl.archetype.core.di.dagger

import android.app.Activity
import android.app.Application
import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

import dagger.android.AndroidInjection
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection

/**
 * @author Denis Makovskyi
 */

object Injector {

    @JvmStatic
    fun <T : Application> init(app: T) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, state: Bundle?) {
                activity::class.annotations
                    .find { it.annotationClass == Injectable::class }
                    ?.let { AndroidInjection.inject(activity) }
                //--
                if (activity is FragmentActivity && activity is HasAndroidInjector) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fragmentManager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
                            fragment::class.annotations
                                .find { it.annotationClass == Injectable::class }
                                ?.let { AndroidSupportInjection.inject(fragment) }
                        }
                    }, true)
                }
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, state: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}