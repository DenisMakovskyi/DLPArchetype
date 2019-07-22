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

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
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

    protected fun setupToolbar(
        toolbar: Toolbar,
        iconResId: Int = 0,
        iconDrawable: Drawable? = null,
        text: String? = null,
        onNavigationClickListener: View.OnClickListener? = null) {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayShowHomeEnabled(true)
        }
        when {
            iconResId != 0 && iconDrawable == null -> toolbar.setNavigationIcon(iconResId)
            iconResId == 0 && iconDrawable != null -> toolbar.navigationIcon = iconDrawable
            else -> toolbar.navigationIcon = null
        }
        text?.let { toolbar.title = it }
        onNavigationClickListener?.let { toolbar.setNavigationOnClickListener(it) }
    }

    protected fun startActivity(cls: Class<out Activity>, bundle: Bundle? = null, extras: Intent? = null) =
        createActivityLaunchIntent(cls).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }.let { startActivity(it) }

    protected fun startActivityForResult(requestCode: Int, cls: Class<out Activity>, bundle: Bundle? = null, extras: Intent? = null) =
        createActivityLaunchIntent(cls).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }.let { startActivityForResult(it, requestCode) }

    protected fun createActivityLaunchIntent(cls: Class<out Activity>): Intent = Intent(this, cls)

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "BaseActivity::isServiceRunning - this method is only intended for debugging or implementing service management type user interfaces",
        replaceWith = ReplaceWith("", ""))
    protected fun <T : Service> isServiceRunning(serviceClass: Class<T>): Boolean =
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Int.MAX_VALUE)
            .has { serviceClass.name == it.service.className }

    protected fun startService(cls: Class<out Service>): ComponentName? =
        startService(Intent(this, cls))

    protected fun stopService(cls: Class<out Service>): Boolean =
        stopService(Intent(this, cls))

    protected fun restartService(cls: Class<out Service>): ComponentName? =
        stopService(cls).let { if (it) startService(cls) else null }

    protected fun bindService(serviceClass: Class<out Service>, serviceConnection: ServiceConnection, flags: Int): Boolean =
        bindService(Intent(this, serviceClass), serviceConnection, flags)

    protected fun <T : Fragment> findFragment(@IdRes containerId: Int): Optional<T> =
        Optional.ofNullable(supportFragmentManager.findFragmentById(containerId) as T)

    protected fun <T : Fragment> findFragment(cls: Class<T>): Optional<T> =
        Optional.ofNullable(supportFragmentManager.findFragmentByTag(cls.name) as T)

    protected fun addFragment(@IdRes containerId: Int, fragment: Fragment, addToBackStack: Boolean = false, allowStateLoss: Boolean = true) {
        createFragmentTransaction(containerId, fragment::class.java.name, fragment, addToBackStack).apply {
            if (allowStateLoss) commitAllowingStateLoss() else commit()
        }
    }

    private fun createFragmentTransaction(@IdRes containerId: Int, tag: String, fragment: Fragment, addToBackStack: Boolean = false): FragmentTransaction =
        supportFragmentManager
            .beginTransaction()
            .replace(containerId, fragment, tag).apply {
                if (addToBackStack) addToBackStack(tag)
            }
}