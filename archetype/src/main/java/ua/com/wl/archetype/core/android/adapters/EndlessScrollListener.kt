package ua.com.wl.archetype.core.android.adapters

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import ua.com.wl.archetype.utils.findLastVisibleItemPosition

/**
 * @author Denis Makovskyi
 */

class EndlessScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private var startPage: Int = 0,
    private var visibleThreshold: Int = 0,
    private val onLoadMoreListener: OnLoadMoreListener

) : RecyclerView.OnScrollListener() {

    interface OnLoadMoreListener {

        fun onLoadMore(page: Int)

        fun onLoadingStateChanged(isLoading: Boolean)
    }

    private var isLoading: Boolean = false
    private var currentPage: Int = 0
    private var previousTotalItemCount: Int = 0

    init {
        when (layoutManager) {
            is GridLayoutManager -> visibleThreshold *= layoutManager.spanCount
            is StaggeredGridLayoutManager -> visibleThreshold *= layoutManager.spanCount
        }
        resetState()
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = layoutManager.itemCount
        if (isUserScrolled(view)) {
            if (isLoadingFinished(totalItemCount)) {
                isLoading = false
                onLoadMoreListener.onLoadingStateChanged(isLoading)

            } else if (shouldStartLoading(layoutManager.findLastVisibleItemPosition(), totalItemCount)) {
                isLoading = true
                currentPage++
                onLoadMoreListener.onLoadMore(currentPage)
                onLoadMoreListener.onLoadingStateChanged(isLoading)
            }
        }

        this.previousTotalItemCount = totalItemCount
    }

    fun onLoadingFinished() {
        if (isLoadingFinished(layoutManager.itemCount)) {
            isLoading = false
            onLoadMoreListener.onLoadingStateChanged(isLoading)
        }
    }

    private fun resetState() {
        isLoading = false
        currentPage = startPage
        previousTotalItemCount = 0
    }

    private fun isUserScrolled(view: RecyclerView) =
        view.scrollState != RecyclerView.SCROLL_STATE_IDLE

    private fun shouldStartLoading(lastVisiblePosition: Int, totalItemCount: Int) =
        !isLoading && lastVisiblePosition + visibleThreshold > totalItemCount

    private fun isLoadingFinished(totalItemCount: Int) =
        isLoading && totalItemCount > (previousTotalItemCount + 1)
}