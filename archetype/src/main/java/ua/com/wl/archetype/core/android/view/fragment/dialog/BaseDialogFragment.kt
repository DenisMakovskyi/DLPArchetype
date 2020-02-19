package ua.com.wl.archetype.core.android.view.fragment.dialog

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle

import androidx.annotation.IdRes
import androidx.fragment.app.*

import ua.com.wl.archetype.core.android.view.activity.BaseActivity
import ua.com.wl.archetype.core.android.view.fragment.FragmentTransactionType
import ua.com.wl.archetype.utils.has
import ua.com.wl.archetype.utils.Optional

/**
 * @author Denis Makovskyi
 */

open class BaseDialogFragment: DialogFragment() {

    val baseActivity: BaseActivity?
        get() = if (activity is BaseActivity) activity as BaseActivity else null

    inline fun <reified A: Activity> startActivity(
        bundle: Bundle? = null,
        extras: Intent? = null
    ) {
        val intent = createActivityLaunchIntent<A>().apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }
        startActivity(intent)
    }

    inline fun <reified A: Activity> startActivityForResult(
        code: Int,
        bundle: Bundle? = null,
        extras: Intent? = null
    ) {
        val intent = createActivityLaunchIntent<A>().apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }
        startActivityForResult(intent, code)
    }

    inline fun <reified A: Activity> createActivityLaunchIntent(): Intent {
        return Intent(activity, A::class.java)
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "BaseDialogFragment::isServiceRunning - this method is only intended for debugging or implementing service management type user interfaces",
        replaceWith = ReplaceWith("", "")
    )
    inline fun <reified S : Service> isServiceRunning(): Boolean {
        val activityManager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        return runningServices.has { S::class.java.name == it.service.className }
    }

    inline fun <reified S: Service> stopService(): Boolean {
        return activity?.stopService(Intent(activity, S::class.java)) ?: false
    }

    inline fun <reified S: Service> startService(): ComponentName? {
        return activity?.startService(Intent(activity, S::class.java))
    }

    inline fun <reified S: Service> restartService(): ComponentName? {
        val isStopped = stopService<S>()
        return if (isStopped) startService<S>() else null
    }

    inline fun <reified S: Service> bindService(
        flags: Int,
        serviceConnection: ServiceConnection
    ): Boolean {
        return activity?.bindService(
            Intent(activity, S::class.java), serviceConnection, flags) ?: false
    }

    fun unbindService(serviceConnection: ServiceConnection) {
        activity?.unbindService(serviceConnection)
    }

    inline fun <reified F: Fragment> addFragment(
        @IdRes containerId: Int,
        tag: String? = F::class.java.name,
        args: Bundle? = null,
        addToBackStack: Boolean = false,
        allowStateLoss: Boolean = true
    ) {
        beginFragmentTransaction<F>(
            containerId, tag, args, addToBackStack,
            FragmentTransactionType.ADD
        ).apply {
            if (allowStateLoss) commit() else commitNowAllowingStateLoss()
        }
    }

    inline fun <reified F: Fragment> replaceFragment(
        @IdRes containerId: Int,
        tag: String? = F::class.java.name,
        args: Bundle? = null,
        addToBackStack: Boolean = false,
        allowStateLoss: Boolean = true
    ) {
        beginFragmentTransaction<F>(
            containerId, tag, args, addToBackStack,
            FragmentTransactionType.REPLACE
        ).apply {
            if (allowStateLoss) commit() else commitNowAllowingStateLoss()
        }
    }

    inline fun <reified F: Fragment> beginFragmentTransaction(
        @IdRes containerId: Int,
        tag: String? = null,
        args: Bundle? = null,
        addToBackStack: Boolean = false, transactionType: FragmentTransactionType = FragmentTransactionType.REPLACE
    ): FragmentTransaction {
        return parentFragmentManager.beginTransaction()
            .apply {
                when (transactionType) {
                    FragmentTransactionType.ADD -> add<F>(containerId, tag, args)
                    FragmentTransactionType.REPLACE -> replace<F>(containerId, tag, args)
                }
                if (addToBackStack) addToBackStack(tag)
            }
    }

    fun popBackStack() {
        parentFragmentManager.popBackStack()
    }

    fun popBackStackImmediate(): Boolean {
        return parentFragmentManager.popBackStackImmediate()
    }

    fun popBackStackInclusive(id: Int) {
        return parentFragmentManager.popBackStack(
            id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun popBackStackInclusive(name: String?) {
        return parentFragmentManager.popBackStack(
            name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun popBackStackImmediateInclusive(id: Int): Boolean {
        return parentFragmentManager.popBackStackImmediate(
            id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun popBackStackImmediateInclusive(name: String?): Boolean {
        return parentFragmentManager.popBackStackImmediate(
            name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    inline fun <reified T : Fragment> findFragment(@IdRes containerId: Int): Optional<T> {
        val fragment = parentFragmentManager.findFragmentById(containerId)
        return if (fragment is T?) {
            Optional.ofNullable(fragment)
        } else {
            Optional.empty()
        }
    }

    inline fun <reified T : Fragment> findFragment(clazz: Class<T>): Optional<T> {
        val fragment = parentFragmentManager.findFragmentByTag(clazz.name)
        return if (fragment is T?) {
            Optional.ofNullable(fragment)
        } else {
            Optional.empty()
        }
    }
}