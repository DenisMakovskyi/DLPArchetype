package ua.com.wl.archetype.mvvm.view.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import ua.com.wl.archetype.core.android.view.BaseActivity

/**
 * @author Denis Makovskyi
 */

abstract class BindingActivity<B : ViewDataBinding, VM : ActivityViewModel> : BaseActivity() {

    var binding: B? = null
        private set
    var viewModel: VM? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    override fun onStart() {
        super.onStart()
        viewModel?.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel?.onPostCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel?.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        viewModel?.onCreateOptionsMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        viewModel?.onPrepareOptionsMenu(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        viewModel?.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel?.onSaveInstanceState(outState)
    }

    override fun onStop() {
        viewModel?.onStop()
        super.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel?.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.let {
            it.onDestroy()
            lifecycle.removeObserver(it)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        viewModel?.onWindowFocusChanged(hasFocus)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel?.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() = viewModel?.let {
        if (!it.onBackPressed()) super.onBackPressed()

    } ?: super.onBackPressed()

    abstract fun onCreate(): VM

    @IdRes
    abstract fun getVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    protected fun bind() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = viewModel ?: onCreate()
        viewModel?.let { vm ->
            lifecycle.addObserver(vm)
            binding?.let { bind ->
                bind.setVariable(getVariable(), vm)
                bind.executePendingBindings()
            }
        }
    }

    protected fun resetViewModel() {
        viewModel?.let { lifecycle.removeObserver(it) }
        viewModel = null
        viewModel = onCreate()
        viewModel?.let {
            lifecycle.addObserver(it)
            binding?.setVariable(getVariable(), it)
        }
    }
}