package bas.droid.paging

import android.util.Log
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

class PagingLoadStateDispatcher private constructor(
    private val adapterItemCountListener: AdapterItemCountListener,
    private val listener: PagingLoadStateListener
) {

    companion object {

        @JvmStatic
        fun concat(
            adapter: androidx.paging.PagingDataAdapter<*, *>,
            listener: PagingLoadStateListener
        ): PagingLoadStateDispatcher {
            val handler = PagingLoadStateDispatcher(PagingAdapterWrapper(adapter), listener)
            adapter.addLoadStateListener(handler::handleLoadState)
            return handler
        }

        @JvmStatic
        fun concat(
            adapter: androidx.leanback.paging.PagingDataAdapter<*>,
            listener: PagingLoadStateListener
        ): PagingLoadStateDispatcher {
            val handler = PagingLoadStateDispatcher(LeanbackPagingAdapterWrapper(adapter), listener)
            adapter.addLoadStateListener(handler::handleLoadState)
            return handler
        }
    }

    private fun handleLoadState(loadState: CombinedLoadStates) {
        Log.d("PagingLoadState","handleLoadState:${loadState}")
        val refresh = loadState.refresh
        if (refresh is LoadState.Loading) {
            //initial loading
            listener.onFirstLoading()
            return
        }
        if (refresh is LoadState.Error) {
            //initial load failed
            listener.onFirstLoadError(refresh.error)
            return
        }

        val prepend = loadState.prepend
        if (prepend is LoadState.Loading) {
            // loading previous (加载前一页：通常为非第一页开始加载的情况才会出现)
            listener.onPrependLoading()
            return
        }

        if (prepend is LoadState.Error) {
            // loading previous failed (加载前一页：通常为非第一页开始加载的情况才会出现)
            listener.onPrependLoadError(prepend.error)
            return
        }


        val append = loadState.append
        if (append is LoadState.Loading) {
            //loading more
            listener.onAppendLoading()
            return
        }

        if (append is LoadState.Error) {
            //load more failed
            listener.onAppendLoadError(append.error)
            return
        }

        if (append.endOfPaginationReached && adapterItemCountListener.getItemCount() == 0) {
            //empty
            listener.onEmptyState()
            return
        }
        listener.onContentState()
    }

    interface AdapterItemCountListener {
        fun getItemCount(): Int
    }

    private class PagingAdapterWrapper(val adapter: androidx.paging.PagingDataAdapter<*, *>) :
        AdapterItemCountListener {
        override fun getItemCount(): Int {
            return adapter.itemCount
        }
    }

    private class LeanbackPagingAdapterWrapper(val adapter: androidx.leanback.paging.PagingDataAdapter<*>) :
        AdapterItemCountListener {
        override fun getItemCount(): Int {
            return adapter.size()
        }
    }
}