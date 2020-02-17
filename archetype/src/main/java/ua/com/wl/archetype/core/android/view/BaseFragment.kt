package ua.com.wl.archetype.core.android.view

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import ua.com.wl.archetype.utils.has
import ua.com.wl.archetype.utils.Optional

/**
 * @author Denis Makovskyi
 */

open class BaseFragment : Fragment() {

    val baseActivity: BaseActivity?
        get() = if (activity is BaseActivity) activity as BaseActivity else null

    var supportActionBar: ActionBar? = baseActivity?.supportActionBar

    fun setSupportActionBar(toolbar: Toolbar) = baseActivity?.setSupportActionBar(toolbar)

    fun setupToolbar(
        toolbar: Toolbar,
        showHome: Boolean = false,
        homeAsUp: Boolean = false,
        showLogo: Boolean = false,
        showTitle: Boolean = false,
        showCustom: Boolean = false,
        @DrawableRes iconResId: Int = 0,
        iconDrawable: Drawable? = null,
        @DrawableRes logoResId: Int = 0,
        logoDrawable: Drawable? = null,
        toolbarTitleText: String? = null,
        onNavigationClickListener: View.OnClickListener? = null
    ) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(showHome)
            setDisplayHomeAsUpEnabled(homeAsUp)
            setDisplayUseLogoEnabled(showLogo)
            setDisplayShowTitleEnabled(showTitle)
            setDisplayShowCustomEnabled(showCustom)
        }
        toolbar.apply {
            // - nav icon
            navigationIcon = iconDrawable
            if (iconResId != 0) {
                setNavigationIcon(iconResId)
            }
            // - toolbar logo
            logo = logoDrawable
            if (logoResId != 0) {
                setLogo(logoResId)
            }
            // - toolbar title
            title = toolbarTitleText
            // - nav click listener
            setNavigationOnClickListener(onNavigationClickListener)
        }
    }

    fun startActivity(
        clazz: Class<out Activity>,
        bundle: Bundle? = null,
        extras: Intent? = null
    ) {
        val intent = createActivityLaunchIntent(clazz).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }
        startActivity(intent)
    }

    fun startActivityForResult(
        code: Int,
        clazz: Class<out Activity>,
        bundle: Bundle? = null,
        extras: Intent? = null
    ) {
        val intent = createActivityLaunchIntent(clazz).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }
        startActivityForResult(intent, code)
    }

    fun createActivityLaunchIntent(clazz: Class<out Activity>): Intent {
        return Intent(requireActivity(), clazz)
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "BaseActivity::isServiceRunning - this method is only intended for debugging or implementing service management type user interfaces",
        replaceWith = ReplaceWith("", ""))
    fun <T : Service> isServiceRunning(serviceClass: Class<T>): Boolean {
        val activityManager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        return runningServices.has { serviceClass.name == it.service.className }
    }

    fun stopService(clazz: Class<out Service>): Boolean {
        return activity?.stopService(Intent(activity, clazz)) ?: false
    }

    fun startService(clazz: Class<out Service>): ComponentName? {
        return activity?.startService(Intent(activity, clazz))
    }

    fun restartService(cls: Class<out Service>): ComponentName? {
        val isStopped = stopService(cls)
        return if (isStopped) startService(cls) else null
    }

    fun bindService(
        flags: Int,
        clazz: Class<out Service>,
        serviceConnection: ServiceConnection
    ): Boolean {
        return activity?.bindService(Intent(activity, clazz), serviceConnection, flags) ?: false
    }

    fun unbindService(serviceConnection: ServiceConnection) = activity?.unbindService(serviceConnection)


    fun addFragment(
        @IdRes containerId: Int,
        clazz: Class<out Fragment>,
        arguments: Bundle? = null,
        addToBackStack: Boolean = false,
        allowStateLoss: Boolean = true,
        transactionType: FragmentTransactionType = FragmentTransactionType.REPLACE
    ) {
        addFragment(
            containerId,
            clazz.newInstance(),
            arguments,
            addToBackStack,
            allowStateLoss,
            transactionType)
    }

    fun addFragment(
        @IdRes containerId: Int,
        fragment: Fragment,
        arguments: Bundle? = null,
        addToBackStack: Boolean = false,
        allowStateLoss: Boolean = true,
        transactionType: FragmentTransactionType = FragmentTransactionType.REPLACE
    ) {
        arguments?.let {
            fragment.arguments = it
        }
        createFragmentTransaction(
            containerId,
            fragment::class.java.name,
            fragment,
            addToBackStack,
            transactionType
        ).apply {
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
        return parentFragmentManager.beginTransaction()
            .apply {
                when (transactionType) {
                    FragmentTransactionType.ADD -> add(containerId, fragment, tag)
                    FragmentTransactionType.REPLACE -> replace(containerId, fragment, tag)
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