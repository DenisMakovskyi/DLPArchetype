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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import ua.com.wl.archetype.utils.Optional
import ua.com.wl.archetype.utils.has

/**
 * @author Denis Makovskyi
 */

open class BaseDialogFragment: DialogFragment() {

    val baseActivity: BaseActivity?
        get() = if (activity is BaseActivity) activity as BaseActivity else null

    fun startActivity(
        cls: Class<out Activity>,
        bundle: Bundle? = null,
        extras: Intent? = null
    ) {
        val intent = createActivityLaunchIntent(cls).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }
        startActivity(intent)
    }

    fun startActivityForResult(
        requestCode: Int,
        cls: Class<out Activity>,
        bundle: Bundle? = null,
        extras: Intent? = null
    ) {
        val intent = createActivityLaunchIntent(cls).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }
        startActivityForResult(intent, requestCode)
    }

    fun createActivityLaunchIntent(cls: Class<out Activity>): Intent = Intent(activity, cls)

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "BaseActivity::isServiceRunning - this method is only intended for debugging or implementing service management type user interfaces",
        replaceWith = ReplaceWith("", ""))
    fun <T : Service> isServiceRunning(serviceClass: Class<T>): Boolean {
        val activityManager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        return runningServices.has { serviceClass.name == it.service.className }
    }

    fun startService(cls: Class<out Service>): ComponentName? =
        activity?.startService(Intent(activity, cls))

    fun stopService(cls: Class<out Service>): Boolean =
        activity?.stopService(Intent(activity, cls)) ?: false

    fun restartService(cls: Class<out Service>): ComponentName? =
        stopService(cls).let { if (it) startService(cls) else null }

    fun bindService(cls: Class<out Service>, serviceConnection: ServiceConnection, flags: Int): Boolean =
        activity?.bindService(Intent(activity, cls), serviceConnection, flags) ?: false

    fun unbindService(serviceConnection: ServiceConnection) = activity?.unbindService(serviceConnection)

    fun <T : Fragment> findFragment(@IdRes containerId: Int): Optional<T> =
        Optional.ofNullable(childFragmentManager.findFragmentById(containerId) as T?)

    fun <T : Fragment> findFragment(cls: Class<T>): Optional<T> =
        Optional.ofNullable(childFragmentManager.findFragmentByTag(cls.name) as T?)

    fun addFragment(
        @IdRes containerId: Int,
        cls: Class<out Fragment>,
        arguments: Bundle? = null,
        addToBackStack: Boolean = false,
        allowStateLoss: Boolean = true,
        transactionType: FragmentTransactionType = FragmentTransactionType.REPLACE
    ) {
        addFragment(containerId, cls.newInstance(), arguments, addToBackStack, allowStateLoss, transactionType)
    }

    fun addFragment(
        @IdRes containerId: Int,
        fragment: Fragment,
        arguments: Bundle? = null,
        addToBackStack: Boolean = false,
        allowStateLoss: Boolean = true,
        transactionType: FragmentTransactionType = FragmentTransactionType.REPLACE
    ) {
        fragment.apply {
            arguments?.let { setArguments(it) }
        }
        createFragmentTransaction(containerId, fragment::class.java.name, fragment, addToBackStack, transactionType).apply {
            if (allowStateLoss) commitAllowingStateLoss() else commit()
        }
    }

    fun createFragmentTransaction(
        @IdRes containerId: Int,
        tag: String,
        fragment: Fragment,
        addToBackStack: Boolean = false,
        transactionType: FragmentTransactionType = FragmentTransactionType.REPLACE
    ): FragmentTransaction {
        return childFragmentManager.beginTransaction().apply {
            when(transactionType) {
                FragmentTransactionType.ADD -> add(containerId, fragment, tag)
                FragmentTransactionType.REPLACE -> replace(containerId, fragment, tag)
            }
            if (addToBackStack) addToBackStack(tag)
        }
    }

    fun popBackStackInclusive(id: Int) {
        childFragmentManager.popBackStack(id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun popBackStackInclusive(name: String?) {
        childFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}