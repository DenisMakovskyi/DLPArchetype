package ua.com.wl.archetype.core.android.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author Denis Makovskyi
 */

open class ViewPagerAdapter(fragmentManager: FragmentManager, behavior: Int) : FragmentPagerAdapter(fragmentManager, behavior) {

    private val titles: MutableList<String> = mutableListOf()
    private val fragments: MutableList<Fragment> = mutableListOf()

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getItemPosition(obj: Any): Int = fragments.indexOf(obj as Fragment)

    override fun getCount(): Int = fragments.size
}