package ua.com.wl.archetype.mvvm.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import android.content.res.Configuration

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import ua.com.wl.archetype.core.android.view.BaseActivity

/**
 * @author Denis Makovskyi
 */

abstract class BindingActivity<B : ViewDataBinding, VM : ActivityLifecycleCallbacks> : BaseActivity() {

    var binding: B? = null
        private set

    var viewModel: VM? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        requestViewModel()?.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        requestViewModel()?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        requestViewModel()?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        requestViewModel()?.onPostCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        requestViewModel()?.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        requestViewModel()?.onCreateOptionsMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        requestViewModel()?.onPrepareOptionsMenu(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        requestViewModel()?.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        requestViewModel()?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        requestViewModel()?.onStop()
        super.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        requestViewModel()?.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        requestViewModel()?.onDestroy()
        lifecycle.removeObserver(requireViewModel())
        super.onDestroy()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        requestViewModel()?.onWindowFocusChanged(hasFocus)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        requestViewModel()?.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestViewModel()?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (!requireViewModel().onBackPressed()) super.onBackPressed()
    }

    abstract fun onBind(savedInstanceState: Bundle?): VM

    @IdRes
    abstract fun getVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun requireViewModel(rebind: Boolean = false): VM {
        if (viewModel == null && rebind) bind(null)
        return requireNotNull(viewModel)
    }

    fun requestViewModel(rebind: Boolean = false): VM? {
        if (viewModel == null && rebind) bind(null)
        return viewModel
    }

    private fun bind(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = onBind(savedInstanceState)
        //-
        lifecycle.addObserver(requireViewModel())
        //-
        binding?.lifecycleOwner = this
        binding?.setVariable(getVariable(), requireViewModel())
        binding?.executePendingBindings()
    }
}