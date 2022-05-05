package bas.droid.paging


interface PagingLoadStateListener {
    /**
     * 初次加载
     */
    fun onFirstLoading()

    /**
     * 初次加载出错
     */
    fun onFirstLoadError(e: Throwable)

    /**
     * 正在加载更多
     */
    fun onAppendLoading()

    /**
     * 加载更多出错
     */
    fun onAppendLoadError(e: Throwable)

    /**
     * 正在向前加载（加载当前页的前一页）
     */
    fun onPrependLoading()

    /**
     * 向前加载出错（加载当前页的前一页）
     */
    fun onPrependLoadError(e: Throwable)

    /**
     * 列表无数据
     */
    fun onEmptyState()

    /**
     * 显示列表
     */
    fun onContentState()

    fun interface SimpleLoadingCallback {
        /**
         * 正在刷新
         * @param isPullDown true:下拉刷新  false:上拉刷新
         */
        fun onPagingLoading(isPullDown: Boolean)
    }

    fun interface SimpleErrorCallback {
        /**
         * 加载失败
         * @param isPullDown true:下拉刷新  false:上拉刷新
         */
        fun onPagingLoadError(isPullDown: Boolean, e: Throwable)
    }

    companion object {

        /**
         * 创建常用的回调方式
         */
        fun newSimple(
            loading: SimpleLoadingCallback?,
            error: SimpleErrorCallback?,
            empty: (() -> Unit)?,
            content: (() -> Unit)?
        ): PagingLoadStateListener {
            return object : PagingLoadStateListener {
                override fun onFirstLoading() {
                    loading?.onPagingLoading(true)
                }

                override fun onFirstLoadError(e: Throwable) {
                    error?.onPagingLoadError(true, e)
                }

                override fun onAppendLoading() {
                    loading?.onPagingLoading(false)
                }

                override fun onAppendLoadError(e: Throwable) {
                    error?.onPagingLoadError(false, e)
                }

                override fun onPrependLoading() {
                    loading?.onPagingLoading(true)
                }

                override fun onPrependLoadError(e: Throwable) {
                    error?.onPagingLoadError(true, e)
                }

                override fun onEmptyState() {
                    empty?.invoke()
                }

                override fun onContentState() {
                    content?.invoke()
                }
            }
        }
    }
}