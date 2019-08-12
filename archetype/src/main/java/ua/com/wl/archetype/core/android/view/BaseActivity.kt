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

import org.greenrobot.eventbus.Subscribe

import ua.com.wl.archetype.core.android.bus.Bus
import ua.com.wl.archetype.core.android.bus.IgnoreEvent
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

    fun setupToolbar(
        toolbar: Toolbar,
        useLogo: Boolean = false,
        showHome: Boolean = false,
        homeAsUp: Boolean = false,
        showTitle: Boolean = false,
        showCustom: Boolean = false,
        iconResId: Int = 0,
        iconDrawable: Drawable? = null,
        toolbarTitleText: String? = null,
        onNavigationClickListener: View.OnClickListener? = null) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayUseLogoEnabled(useLogo)
            setDisplayShowHomeEnabled(showHome)
            setDisplayHomeAsUpEnabled(homeAsUp)
            setDisplayShowTitleEnabled(showTitle)
            setDisplayShowCustomEnabled(showCustom)
        }
        if (iconResId != 0) {
            toolbar.setNavigationIcon(iconResId)
        }
        iconDrawable?.let {
            toolbar.navigationIcon = it
        }
        toolbarTitleText?.let {
            toolbar.title = it
        }
        onNavigationClickListener?.let {
            toolbar.setNavigationOnClickListener(it)
        }
    }

    fun startActivity(cls: Class<out Activity>, bundle: Bundle? = null, extras: Intent? = null) =
        createActivityLaunchIntent(cls).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }.let { startActivity(it) }

    fun startActivityForResult(requestCode: Int, cls: Class<out Activity>, bundle: Bundle? = null, extras: Intent? = null) =
        createActivityLaunchIntent(cls).apply {
            bundle?.let { putExtras(it) }
            extras?.let { putExtras(it) }
        }.let { startActivityForResult(it, requestCode) }

    fun createActivityLaunchIntent(cls: Class<out Activity>): Intent = Intent(this, cls)

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "BaseActivity::isServiceRunning - this method is only intended for debugging or implementing service management type user interfaces",
        replaceWith = ReplaceWith("", ""))
    fun <T : Service> isServiceRunning(serviceClass: Class<T>): Boolean =
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Int.MAX_VALUE)
            .has { serviceClass.name == it.service.className }

    fun startService(cls: Class<out Service>): ComponentName? =
        startService(Intent(this, cls))

    fun stopService(cls: Class<out Service>): Boolean =
        stopService(Intent(this, cls))

    fun restartService(cls: Class<out Service>): ComponentName? =
        stopService(cls).let { if (it) startService(cls) else null }

    fun bindService(serviceClass: Class<out Service>, serviceConnection: ServiceConnection, flags: Int): Boolean =
        bindService(Intent(this, serviceClass), serviceConnection, flags)

    fun <T : Fragment> findFragment(@IdRes containerId: Int): Optional<T> =
        Optional.ofNullable(supportFragmentManager.findFragmentById(containerId) as T)

    fun <T : Fragment> findFragment(cls: Class<T>): Optional<T> =
        Optional.ofNullable(supportFragmentManager.findFragmentByTag(cls.name) as T)

    fun addFragment(@IdRes containerId: Int, fragment: Fragment, addToBackStack: Boolean = false, allowStateLoss: Boolean = true) {
        createFragmentTransaction(containerId, fragment::class.java.name, fragment, addToBackStack).apply {
            if (allowStateLoss) commitAllowingStateLoss() else commit()
        }
    }

    fun createFragmentTransaction(@IdRes containerId: Int, tag: String, fragment: Fragment, addToBackStack: Boolean = false): FragmentTransaction =
        supportFragmentManager
            .beginTransaction()
            .replace(containerId, fragment, tag).apply {
                if (addToBackStack) addToBackStack(tag)
            }
}