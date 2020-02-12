package ua.com.wl.archetype.mvvm.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import ua.com.wl.archetype.core.android.view.BaseFragment

/**
 * @author Denis Makovskyi
 */

abstract class BindingFragment<B : ViewDataBinding, VM : FragmentViewModel> : BaseFragment() {

    lateinit var binding: B
        private set

    lateinit var viewModel: VM
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = onBind(savedInstanceState, binding)
        //-
        lifecycle.addObserver(viewModel)
        //-
        binding.setVariable(getVariable(), viewModel)
        binding.executePendingBindings()
        //-
        viewModel.onViewCreated()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (::viewModel.isInitialized) {
            viewModel.onActivityCreated()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (::viewModel.isInitialized) {
            viewModel.onViewStateRestored(savedInstanceState)
        }
    }

    override fun onStart() {
        if (::viewModel.isInitialized) {
            viewModel.onStart()
        }
        super.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (::viewModel.isInitialized) {
            viewModel = onBind(null, binding)
            viewModel.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized) {
            viewModel.onResume()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (::viewModel.isInitialized) {
            viewModel.onCreateOptionsMenu()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (::viewModel.isInitialized) {
            viewModel.onPrepareOptionsMenu()
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        if (::viewModel.isInitialized) {
            viewModel.onPause()
        }
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::viewModel.isInitialized) {
            viewModel.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        if (::viewModel.isInitialized) {
            viewModel.onStop()
        }
        super.onStop()
    }

    override fun onDestroyView() {
        if (::viewModel.isInitialized) {
            viewModel.onDestroyView()
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (::viewModel.isInitialized) {
            viewModel.onDestroy()
            lifecycle.removeObserver(viewModel)
        }
        super.onDestroy()
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

    abstract fun onBind(savedInstanceState: Bundle?, binding: B): VM

    @IdRes
    abstract fun getVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int
}