package ua.com.wl.archetype.mvvm.view.fragment.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Intent

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import ua.com.wl.archetype.core.android.view.fragment.dialog.BaseDialogFragment

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
abstract class BindingDialogFragment <B : ViewDataBinding, VM : DialogFragmentLifecycleCallbacks> : BaseDialogFragment() {

    var binding: B? = null
        private set

    var viewModel: VM? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = onBind(savedInstanceState, binding)
        //-
        lifecycle.addObserver(requireViewModel())
        //-
        binding?.lifecycleOwner = this
        binding?.setVariable(getVariable(), requireViewModel())
        binding?.executePendingBindings()
        //-
        requestViewModel()?.onViewCreated()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestViewModel()?.onActivityCreated()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        requestViewModel()?.onViewStateRestored(savedInstanceState)
    }

    override fun onStart() {
        requestViewModel()?.onStart()
        super.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        requestViewModel(true, binding)?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        requestViewModel()?.onResume()
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

    override fun onDestroyView() {
        requestViewModel()?.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        requestViewModel()?.onDestroy()
        val vm = requestViewModel()
        if (vm != null) lifecycle.removeObserver(vm)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestViewModel()?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    abstract fun onBind(savedInstanceState: Bundle?, binding: B?): VM

    @IdRes
    abstract fun getVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun requireViewModel(
        rebind: Boolean = false,
        binding: B? = null
    ): VM {
        if (viewModel == null && rebind) {
            viewModel = onBind(null, binding)
        }
        return requireNotNull(viewModel)
    }

    fun requestViewModel(
        rebind: Boolean = false,
        binding: B? = null
    ): VM? {
        if (viewModel == null && rebind) {
            viewModel = onBind(null, binding)
        }
        return viewModel
    }
}