@file:Suppress("UNCHECKED_CAST")

package ua.com.wl.archetype.core.android.adapters

import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

import ua.com.wl.archetype.utils.whenIndex

/**
 * @author Denis Makovskyi
 */

open class LazyBindingAdapter : RecyclerView.Adapter<LazyBindingAdapter.ViewHolder>() {

    interface OnClickListener<in T> {

        fun onClick(position: Int, view: View, item: T)
    }

    interface PaginationListener {

        fun onNextPage(page: Int)
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

    companion object {

        private const val VIEW_TYPE_UNDEFINED: Int = -1
    }

    private val items: MutableList<Any> = arrayListOf()
    private val variables: MutableMap<Int, Any> = hashMapOf()
    private val resources: MutableMap<Class<out Any>, ItemResource> = hashMapOf()

    private var pagination: Boolean = false
    private var initialPage: Int = 0
    private var visibleThreshold: Int = 0
    private var paginationModel: Any? = null
    private var paginationListener: PaginationListener? = null

    private var endlessScrollListener: EndlessScrollListener? = null
    private var onLoadMoreListener: EndlessScrollListener.OnLoadMoreListener? = null

    override fun getItemViewType(position: Int): Int =
        getItemResource(getItem(position))?.layoutId ?: VIEW_TYPE_UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            VIEW_TYPE_UNDEFINED -> throw InflateException("There is no layout resource for data type")
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false)).apply {
                variables.entries.forEach { binding?.setVariable(it.key, it.value) }
            }
        }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Any = getItem(position)
        getItemResource(item)?.let {
            if (it.bindingId != 0) {
                viewHolder.binding?.setVariable(it.bindingId, item)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun withVariable(bindingId: Int, obj: Any) {
        this.variables[bindingId] = obj
    }

    fun withResource(cls: Class<out Any>, @LayoutRes layoutId: Int, bindingId: Int) {
        this.resources[cls] = ItemResource(layoutId, bindingId)
    }

    fun withPagination(
        initialPage: Int = 0,
        visibleThreshold: Int = 0,
        paginationModel: Any,
        paginationLayoutId: Int,
        paginationBindingId: Int,
        paginationListener: PaginationListener) {
        this.pagination = true
        this.initialPage = initialPage
        this.visibleThreshold = visibleThreshold
        this.paginationModel = paginationModel
        this.paginationListener = paginationListener
        withResource(paginationModel::class.java, paginationLayoutId, paginationBindingId)
    }

    fun withRecyclerView(view: RecyclerView) {
        if (pagination) {
            onLoadMoreListener = object: EndlessScrollListener.OnLoadMoreListener {
                override fun onLoadMore(page: Int) {
                    paginationListener?.onNextPage(page)
                }

                override fun onLoadingStateChanged(isLoading: Boolean) {
                    if (isLoading) notifyLoadMore() else notifyMoreLoaded()
                }
            }
            endlessScrollListener = EndlessScrollListener(
                layoutManager = requireNotNull(view.layoutManager),
                startPage = initialPage,
                visibleThreshold = visibleThreshold,
                onLoadMoreListener = requireNotNull(onLoadMoreListener))
        }
        view.adapter = this
    }

    fun isEmpty(): Boolean = items.isEmpty()

    fun isNotEmpty(): Boolean = items.isNotEmpty()

    fun getItemPosition(item: Any): Int = items.indexOf(item)

    fun getItemResource(item: Any): ItemResource? = resources.entries.find { it.key.isInstance(item) }?.value

    fun <T : Any> getItem(position: Int): T = items[position] as T

    fun <T : Any> getLastItem(): T = items[items.lastIndex] as T

    fun <T : Any> getItems(cls: Class<T>): List<T> {
        val mutableList: MutableList<T> = mutableListOf()
        items.forEach {
            if (cls.isInstance(it)) {
                mutableList.add(it as T)
            }
        }
        return mutableList.toList()
    }

    fun <T : Any> notifyData(anotherItems: List<T>) {
        items.clear()
        items.addAll(anotherItems)
        endlessScrollListener?.onLoadingFinished()
        notifyDataSetChanged()
    }

    fun <T : Any> addItem(item: T) {
        items.add(item)
        endlessScrollListener?.onLoadingFinished()
        notifyItemInserted(items.lastIndex)
    }

    fun <T : Any> addItem(position: Int, item: T) {
        items.add(position, item)
        endlessScrollListener?.onLoadingFinished()
        notifyItemInserted(position)
    }

    fun <T : Any> addItems(newItems: List<T>) {
        val positionStart = items.size + 1
        items.addAll(newItems)
        endlessScrollListener?.onLoadingFinished()
        notifyItemRangeInserted(positionStart, newItems.size)
    }

    fun <T : Any> updateItem(item: T) {
        if (items.isNotEmpty()) {
            val position = getItemPosition(item)
            if (position > -1) notifyItemChanged(position)
        }
    }

    fun <T : Any> updateItem(position: Int, item: T) {
        items.whenIndex(position) {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun <T : Any> removeItem(item: T) {
        if (items.isNotEmpty()) {
            val position = items.indexOf(item)
            if (position > -1) removeItemAt(position)
        }
    }

    fun removeItemAt(position: Int) {
        if (items.isNotEmpty()) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    fun removeItems() {
        items.clear()
        notifyDataSetChanged()
    }

    private fun notifyLoadMore() {
        paginationModel?.let { addItem(it) }
    }

    private fun notifyMoreLoaded() {
        paginationModel?.let {
            if (it == getLastItem()) {
                removeItem(it)
            }
        }
    }
}