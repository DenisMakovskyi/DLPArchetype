@file:Suppress("UNCHECKED_CAST")

package ua.com.wl.archetype.core.android.view

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle

import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import org.greenrobot.eventbus.Subscribe

import ua.com.wl.archetype.core.android.bus.Bus
import ua.com.wl.archetype.core.android.bus.IgnoreEvent
import ua.com.wl.archetype.utils.Optional
import ua.com.wl.archetype.utils.has

/**
 * @author Denis Makovskyi
 */

open class BaseDialogFragment: DialogFragment() {

    val baseActivity: BaseActivity?
        get() = if (activity is BaseActivity) activity as BaseActivity else null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Bus.eventsBus?.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.eventsBus?.unregister(this)
    }

    @Subscribe
    fun onEvent(event: IgnoreEvent) {
    }

    fun startActivity(cls: Class<out Activity>, bundle: Bundle? = null, extras: Intent? = null) =
        createActivityLaunchIntent(cls).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }.let { startActivity(it) }

    fun startActivityForResult(
        requestCode: Int,
        cls: Class<out Activity>,
        bundle: Bundle? = null,
        extras: Intent? = null) =
        createActivityLaunchIntent(cls).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }.let { startActivityForResult(it, requestCode) }

    fun createActivityLaunchIntent(cls: Class<out Activity>): Intent = Intent(activity, cls)

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "BaseBottomSheetDialogFragment::isServiceRunning - this method is only intended for debugging or implementing service management type user interfaces",
        replaceWith = ReplaceWith("", ""))
    fun <T : Service> isServiceRunning(serviceClass: Class<T>): Boolean =
        (activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Int.MAX_VALUE)
            .has { serviceClass.name == it.service.className }

    fun startService(cls: Class<out Service>): ComponentName? =
        activity?.startService(Intent(activity, cls))

    fun stopService(cls: Class<out Service>): Boolean =
        activity?.stopService(Intent(activity, cls)) ?: false

    fun restartService(cls: Class<out Service>): ComponentName? =
        stopService(cls).let { if (it) startService(cls) else null }

    fun bindService(serviceClass: Class<out Service>, serviceConnection: ServiceConnection, flags: Int): Boolean =
        activity?.bindService(Intent(activity, serviceClass), serviceConnection, flags) ?: false

    fun unbindService(serviceConnection: ServiceConnection) = activity?.unbindService(serviceConnection)

    fun <T : Fragment> findFragment(@IdRes containerId: Int): Optional<T> =
        Optional.ofNullable(childFragmentManager.findFragmentById(containerId) as T)

    fun <T : Fragment> findFragment(cls: Class<T>): Optional<T> =
        Optional.ofNullable(childFragmentManager.findFragmentByTag(cls.name) as T)

    fun addFragment(
        @IdRes containerId: Int, fragment: Fragment,
        addToBackStack: Boolean = false,
        allowStateLoss: Boolean = true) {
        createFragmentTransaction(containerId, fragment::class.java.name, fragment, addToBackStack).apply {
            if (allowStateLoss) commitAllowingStateLoss() else commit()
        }
    }

    fun createFragmentTransaction(
        @IdRes containerId: Int, tag: String,
        fragment: Fragment,
        addToBackStack: Boolean = false): FragmentTransaction =
        childFragmentManager
            .beginTransaction()
            .replace(containerId, fragment, tag).apply {
                if (addToBackStack) addToBackStack(tag)
            }
}