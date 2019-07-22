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

    var binding: B? = null
        private set
    var viewModel: VM? = null
        private set
    var savedInstanceState: Bundle? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, instanceState: Bundle?) {
        savedInstanceState = instanceState
        binding?.let { b ->
            viewModel = onCreateViewModel(b)
            viewModel?.let { vm ->
                lifecycle.addObserver(vm)
                b.setVariable(getVariable(), vm)
                b.executePendingBindings()
                vm.onViewCreated()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel?.onActivityCreated()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel?.onViewStateRestored(savedInstanceState)
    }

    override fun onStart() {
        viewModel?.onStart()
        super.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel?.let { vm ->
            binding?.let { b ->
                viewModel = onCreateViewModel(b)
            }
            vm.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel?.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel?.onCreateOptionsMenu()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        viewModel?.onPrepareOptionsMenu()
        super.onPrepareOptionsMenu(menu)
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

    override fun onDestroyView() {
        viewModel?.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.let {
            it.onDestroy()
            lifecycle.removeObserver(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel?.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @IdRes
    abstract fun getVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    protected abstract fun onCreateViewModel(binding: B): VM

    protected fun resetViewModel() {
        viewModel?.let { lifecycle.removeObserver(it) }
        viewModel = null
        binding?.let { viewModel = onCreateViewModel(it) }
        viewModel?.let {
            lifecycle.addObserver(it)
            binding?.setVariable(getVariable(), it)
        }
    }
}