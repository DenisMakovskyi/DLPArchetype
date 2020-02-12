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

    lateinit var binding: B
        private set

    lateinit var viewModel: VM
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (::viewModel.isInitialized) {
            viewModel.onStart()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (::viewModel.isInitialized) {
            viewModel.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (::viewModel.isInitialized) {
            viewModel.onRestoreInstanceState(savedInstanceState)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (::viewModel.isInitialized) {
            viewModel.onPostCreate(savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized) {
            viewModel.onResume()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (::viewModel.isInitialized) {
            viewModel.onCreateOptionsMenu(menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (::viewModel.isInitialized) {
            viewModel.onPrepareOptionsMenu(menu)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        if (::viewModel.isInitialized) {
            viewModel.onPause()
        }
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::viewModel.isInitialized) {
            viewModel.onSaveInstanceState(outState)
        }
    }

    override fun onStop() {
        if (::viewModel.isInitialized) {
            viewModel.onStop()
        }
        super.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (::viewModel.isInitialized) {
            viewModel.onConfigurationChanged(newConfig)
        }
    }

    override fun onDestroy() {
        if (::viewModel.isInitialized) {
            viewModel.onDestroy()
            lifecycle.removeObserver(viewModel)
        }
        super.onDestroy()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (::viewModel.isInitialized) {
            viewModel.onWindowFocusChanged(hasFocus)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (::viewModel.isInitialized) {
            viewModel.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::viewModel.isInitialized) {
            viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onBackPressed() {
        if (::viewModel.isInitialized) {
            if (!viewModel.onBackPressed()) super.onBackPressed()
        }
    }

    abstract fun onBind(savedInstanceState: Bundle?): VM

    @IdRes
    abstract fun getVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    private fun bind(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = onBind(savedInstanceState)
        //-
        lifecycle.addObserver(viewModel)
        //-
        binding.setVariable(getVariable(), viewModel)
        binding.executePendingBindings()
    }
}