package ua.com.wl.archetype.core.android.adapters

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.InflateException

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Denis Makovskyi
 */

abstract class LazyBindingPagedAdapter(diffCallback: DiffUtil.ItemCallback<Any>) :
    PagedListAdapter<Any, LazyBindingPagedAdapter.ViewHolder>(diffCallback) {

    companion object {

        private const val VIEW_TYPE_UNDEFINED: Int = -1
    }

    private val variables: MutableMap<Int, Any> = hashMapOf()
    private val resources: MutableMap<Class<out Any>, ItemResource> = hashMapOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        onRegisterVariables()
        onRegisterResources()
    }

    override fun getItemViewType(position: Int): Int =
        getItem(position)?.let { getItemResource(it) }?.layoutId ?: VIEW_TYPE_UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            VIEW_TYPE_UNDEFINED -> throw InflateException("There is no layout resource for data type")
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false)).apply {
                variables.entries.forEach { binding?.setVariable(it.key, it.value) }
            }
        }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            getItemResource(item)?.let { resource ->
                if (resource.bindingId != 0) {
                    viewHolder.binding?.setVariable(resource.bindingId, item)
                }
            }
        }
    }

    abstract fun onRegisterVariables()

    abstract fun onRegisterResources()

    fun registerVariable(bindingId: Int, obj: Any) {
        variables[bindingId] = obj
    }

    fun registerResource(cls: Class<out Any>, @LayoutRes layoutId: Int, bindingId: Int) {
        resources[cls] = ItemResource(layoutId, bindingId)
    }

    fun getItemResource(item: Any): ItemResource? = resources.entries.find { it.key.isInstance(item) }?.value

    interface OnClickListener<in T> {

        fun onClick(position: Int, view: View, item: T)
    }

    inner class ItemResource(@LayoutRes layoutId: Int, bindingId: Int) {

        var layoutId: Int = layoutId
            private set
        var bindingId: Int = bindingId
            private set
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding: ViewDataBinding? = DataBindingUtil.bind(itemView)
            private set
    }
}