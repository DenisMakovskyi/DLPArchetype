@file:Suppress("UNCHECKED_CAST")

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

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import ua.com.wl.archetype.utils.Optional
import ua.com.wl.archetype.utils.has

/**
 * @author Denis Makovskyi
 */

open class BaseActivity : AppCompatActivity() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

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

    fun createActivityLaunchIntent(cls: Class<out Activity>): Intent = Intent(this, cls)

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "BaseActivity::isServiceRunning - this method is only intended for debugging or implementing service management type user interfaces",
        replaceWith = ReplaceWith("", ""))
    fun <T : Service> isServiceRunning(serviceClass: Class<T>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        return runningServices.has { serviceClass.name == it.service.className }
    }

    fun startService(cls: Class<out Service>): ComponentName? =
        startService(Intent(this, cls))

    fun stopService(cls: Class<out Service>): Boolean =
        stopService(Intent(this, cls))

    fun restartService(cls: Class<out Service>): ComponentName? =
        stopService(cls).let { if (it) startService(cls) else null }

    fun bindService(cls: Class<out Service>, serviceConnection: ServiceConnection, flags: Int): Boolean =
        bindService(Intent(this, cls), serviceConnection, flags)

    fun <T : Fragment> findFragment(@IdRes containerId: Int): Optional<T> =
        Optional.ofNullable(supportFragmentManager.findFragmentById(containerId) as T?)

    fun <T : Fragment> findFragment(cls: Class<T>): Optional<T> =
        Optional.ofNullable(supportFragmentManager.findFragmentByTag(cls.name) as T?)

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
        return supportFragmentManager.beginTransaction().apply {
            when(transactionType) {
                FragmentTransactionType.ADD -> add(containerId, fragment, tag)
                FragmentTransactionType.REPLACE -> replace(containerId, fragment, tag)
            }
            if (addToBackStack) addToBackStack(tag)
        }
    }

    fun popBackStack() =
        supportFragmentManager.popBackStack()

    fun popBackStackImmediate(): Boolean =
        supportFragmentManager.popBackStackImmediate()

    fun popBackStackInclusive(id: Int) =
        supportFragmentManager.popBackStack(id, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    fun popBackStackInclusive(name: String?) =
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    fun popBackStackImmediateInclusive(id: Int): Boolean =
        supportFragmentManager.popBackStackImmediate(id, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    fun popBackStackImmediateInclusive(name: String?): Boolean =
        supportFragmentManager.popBackStackImmediate(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}