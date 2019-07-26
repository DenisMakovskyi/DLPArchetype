package ua.com.wl.archetype.core.android.adapters

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.InflateException

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.viewpager.widget.PagerAdapter

class LazyBindingPagerAdapter(private val items: List<Any>) : PagerAdapter() {

    private val variables: MutableMap<Int, Any> = hashMapOf()
    private val resources: MutableMap<Class<out Any>, ItemResource> = hashMapOf()

    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        getItemResource(getItem(position))?.let { itemResource ->
            BondedView(
                LayoutInflater
                    .from(container.context)
                    .inflate(itemResource.layoutId, container, false))
                .apply {
                    variables.entries.forEach { binding?.setVariable(it.key, it.value) }
                    binding?.setVariable(itemResource.bindingId, getItem(position))
                    container.addView(binding?.root)

                }.let { bondedView ->
                    bondedView.binding?.root ?: throw InflateException("Could not to create view from resource")
                }

        } ?: throw InflateException("Could not to create view from resource")

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) =
        if (obj is View) {
            container.removeView(obj)
        } else {
            container.removeViewAt(position)
        }

    override fun getCount(): Int = items.size

    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

    fun registerVariable(bindingId: Int, obj: Any) {
        variables[bindingId] = obj
    }

    fun registerResource(cls: Class<out Any>, @LayoutRes layoutId: Int, bindingId: Int) {
        resources[cls] = ItemResource(layoutId, bindingId)
    }

    private fun getItem(position: Int): Any = items[position]

    private fun getItemResource(item: Any): ItemResource? = resources.entries.find { it.key.isInstance(item) }?.value

    inner class ItemResource(@LayoutRes layoutId: Int, bindingId: Int) {

        var layoutId: Int = layoutId
            private set
        var bindingId: Int = bindingId
            private set
    }

    inner class BondedView(itemView: View) {

        var binding: ViewDataBinding? = DataBindingUtil.bind(itemView)
            private set
    }
}