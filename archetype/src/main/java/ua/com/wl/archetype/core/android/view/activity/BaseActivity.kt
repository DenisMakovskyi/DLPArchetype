package ua.com.wl.archetype.core.android.view.activity

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
import androidx.annotation.DrawableRes
import androidx.fragment.app.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import ua.com.wl.archetype.core.android.view.fragment.FragmentTransactionType
import ua.com.wl.archetype.utils.has
import ua.com.wl.archetype.utils.Optional

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
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
        return Intent(this, A::class.java)
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "BaseActivity::isServiceRunning - this method is only intended for debugging or implementing service management type user interfaces",
        replaceWith = ReplaceWith("", "")
    )
    inline fun <reified S : Service> isServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        return runningServices.has { S::class.java.name == it.service.className }
    }

    inline fun <reified S: Service> stopService(): Boolean {
        return stopService(Intent(this, S::class.java))
    }

    inline fun <reified S: Service> startService(): ComponentName? {
        return startService(Intent(this, S::class.java))
    }

    inline fun <reified S: Service> restartService(): ComponentName? {
        val isStopped = stopService<S>()
        return if (isStopped) startService<S>() else null
    }

    inline fun <reified S: Service> bindService(
        flags: Int,
        serviceConnection: ServiceConnection
    ): Boolean {
        return bindService(
            Intent(this, S::class.java), serviceConnection, flags)
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
        addToBackStack: Boolean = false,
        transactionType: FragmentTransactionType = FragmentTransactionType.REPLACE
    ): FragmentTransaction {
        return supportFragmentManager.beginTransaction()
            .apply {
                when (transactionType) {
                    FragmentTransactionType.ADD -> add<F>(containerId, tag, args)
                    FragmentTransactionType.REPLACE -> replace<F>(containerId, tag, args)
                }
                if (addToBackStack) addToBackStack(tag)
            }
    }

    fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    fun popBackStackImmediate(): Boolean {
        return supportFragmentManager.popBackStackImmediate()
    }

    fun popBackStackInclusive(id: Int) {
        return supportFragmentManager.popBackStack(
            id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun popBackStackInclusive(name: String?) {
        return supportFragmentManager.popBackStack(
            name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun popBackStackImmediateInclusive(id: Int): Boolean {
        return supportFragmentManager.popBackStackImmediate(
            id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun popBackStackImmediateInclusive(name: String?): Boolean {
        return supportFragmentManager.popBackStackImmediate(
            name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    inline fun <reified T : Fragment> findFragment(@IdRes containerId: Int): Optional<T> {
        val fragment = supportFragmentManager.findFragmentById(containerId)
        return if (fragment is T?) {
            Optional.ofNullable(fragment)
        } else {
            Optional.empty()
        }
    }

    inline fun <reified T : Fragment> findFragment(clazz: Class<T>): Optional<T> {
        val fragment = supportFragmentManager.findFragmentByTag(clazz.name)
        return if (fragment is T?) {
            Optional.ofNullable(fragment)
        } else {
            Optional.empty()
        }
    }
}