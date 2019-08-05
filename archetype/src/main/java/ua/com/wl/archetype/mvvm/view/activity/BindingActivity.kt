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
        viewModel.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.onPostCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        viewModel.onCreateOptionsMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        viewModel.onPrepareOptionsMenu(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        viewModel.onWindowFocusChanged(hasFocus)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (!viewModel.onBackPressed()) super.onBackPressed()
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