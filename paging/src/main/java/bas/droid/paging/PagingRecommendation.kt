package bas.droid.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

object PagingRecommendation {

    const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 2

    const val DEFAULT_INITIAL_PAGE_INDEX = 1

    const val DEFAULT_PAGE_SIZE = 20

//    class PositionalDataSource(private val )

    /**
     * 分页方式 数据源
     * @param initialPageIndex 初始页码
     * @param pageSize 分页大小
     * @param loader 加载器，执行数据加载逻辑
     */
    @JvmStatic
    fun <Value : Any> createPagePagingSource(
        initialPageIndex: Int = DEFAULT_INITIAL_PAGE_INDEX,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        loader: PageDataSource.LoadFunction<Value>
    ): PagingSource<Int, Value> {
        return PageDataSource(initialPageIndex, pageSize, loader)
    }

    /**
     * 分页方式 数据源
     * @param initialPageIndex 初始页码
     * @param pageSize 分页大小
     * @param loader 加载器，执行数据加载逻辑
     */
    open class PageDataSource<Value : Any>(
        private val initialPageIndex: Int = DEFAULT_INITIAL_PAGE_INDEX,
        private val pageSize: Int = DEFAULT_PAGE_SIZE,
        private val loader: LoadFunction<Value>
    ) :
        PagingSource<Int, Value>() {

        override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
            // Try to find the page key of the closest page to anchorPosition, from
            // either the prevKey or the nextKey, but you need to handle nullability
            // here:
            //  * prevKey == null -> anchorPage is the first page.
            //  * nextKey == null -> anchorPage is the last page.
            //  * both prevKey and nextKey null -> anchorPage is the initial page, so
            //    just return null.
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }

        /**
         * 获取前一页索引（第几页）
         * @param params 本次加载使用的参数
         * @param loadedData 本次已加载的数据
         */
        protected open fun getPrevKey(params: LoadParams<Int>, loadedData: List<Value>?): Int? {
            val pageIndex = params.key ?: initialPageIndex
            return if (pageIndex <= 1) null else pageIndex - 1
        }

        /**
         * 获取下一页索引
         * @param params 本次加载使用的参数
         * @param loadedData 本次已加载的数据
         */
        protected open fun getNextKey(params: LoadParams<Int>, loadedData: List<Value>?): Int? {
            val loadSize = params.loadSize
            if (loadedData.isNullOrEmpty() || loadedData.size < params.loadSize) {
                //如果本次加载的数据不足指定的加载大小，则认为没有更多数据了，即没有下一页存在
                return null
            }
            val pageIndex = params.key ?: initialPageIndex
            return pageIndex + loadSize / pageSize //这里不能直接+1，因为初始加载的时候可能是同时加载多页数据
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
            return try {
                val loadSize = params.loadSize
                require(loadSize % pageSize == 0) {
                    "pageSize(=${pageSize})不能被loadSize(=${loadSize})整除，请检查参数配置"
                }
                val pageIndex = params.key ?: initialPageIndex
                val data = loader.load(pageIndex, loadSize)
                LoadResult.Page(
                    data = data.orEmpty(),
                    prevKey = getPrevKey(params, data),
                    nextKey = getNextKey(params, data)
                ).also {
                    Log.d("PageDataSource","pageIndex=${pageIndex} prev=${it.prevKey} next=${it.nextKey} loadSize=${loadSize}")
                }
            } catch (e: Throwable) {
                // Handle errors in this block and return LoadResult.Error if it is an
                // expected error (such as a network failure).
                LoadResult.Error(e)
            }
        }

        fun interface LoadFunction<T> {
            suspend fun load(pageIndex: Int, loadSize: Int): List<T>?
        }
    }
}