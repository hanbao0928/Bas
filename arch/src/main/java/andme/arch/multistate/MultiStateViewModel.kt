package andme.arch.multistate

import andme.arch.app.AMViewModel
import andme.core.statelayout.StateLayout
import andme.core.statelayout.StateView
import android.app.Application
import android.view.View
import androidx.annotation.MainThread
import bas.droid.core.content.ProgressFakerTaskCallback
import bas.droid.core.content.invokeTaskWithProgressFaker
import bas.droid.core.lifecycle.MutableLiveEvent
import bas.lib.core.exception.friendlyMessage
import bas.lib.core.lang.orDefaultIfNullOrEmpty
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Lucio on 2021/3/3.
 * 用于Activity/Fragment的主ViewModel
 * 通过[bindMultiStateViewModel]方法绑定一个[MultiStateLayoutViewModel],从而将两个ViewModel绑定在一起
 */
open class MultiStateViewModel(application: Application) : AMViewModel(application) {

    protected lateinit var multiStateLayoutViewModel: MultiStateLayoutViewModel
        private set

    fun bindMultiStateLayoutViewModel(vm: MultiStateLayoutViewModel) {
        this.multiStateLayoutViewModel = vm
        onMultiStateLayoutViewModelPrepared(vm)
    }

    protected open fun onMultiStateLayoutViewModelPrepared(vm: MultiStateLayoutViewModel) {}

    /**
     * 执行与context相关的操作
     */
    @MainThread
    fun invokeMultiStateAction(ctxAction: MultiStateLayoutViewModel.StateLayoutEventAction) {
        multiStateLayoutViewModel.performStateLayoutAction(ctxAction)
    }

    @MainThread
    fun showMultiStateContentView() {
        multiStateLayoutViewModel.showContentView()
    }

    @MainThread
    fun showMultiStateLoadingView() {
        multiStateLayoutViewModel.showLoadingView()
    }

    @MainThread
    fun showMultiStateLoadingView(msg: String?) {
        showMultiStateLoadingView {
            showLoadingMsg(msg)
        }
    }

    @MainThread
    fun showMultiStateLoadingView(setup: (StateView.() -> Unit)?) {
        multiStateLayoutViewModel.showLoadingView(setup)
    }

    @MainThread
    fun showMultiStateEmptyView() {
        multiStateLayoutViewModel.showEmptyView()
    }

    @MainThread
    fun showMultiStateEmptyView(msg: String) {
        showMultiStateEmptyView {
            showEmptyMsgWithoutButton(msg)
        }
    }

    @MainThread
    fun showMultiStateEmptyView(setup: (StateView.() -> Unit)?) {
        multiStateLayoutViewModel.showEmptyView(setup)
    }

    @MainThread
    fun showMultiStateErrorView() {
        showMultiStateErrorView(RuntimeException("未知异常。"))
    }

    @MainThread
    fun showMultiStateErrorView(e: Throwable) {
        showMultiStateErrorView {
            showErrorMsgWithoutButton(e.friendlyMessage.orDefaultIfNullOrEmpty("未知错误"))
        }
    }

    @MainThread
    fun showMultiStateErrorView(
        e: Throwable,
        buttonText: String = "重试",
        requestFocus: Boolean = false,
        onBtnClick: (View) -> Unit
    ) {
        showMultiStateErrorView {
            showErrorMsgWithButton(
                e.friendlyMessage.orDefaultIfNullOrEmpty("未知错误"),
                buttonText,
                requestFocus,
                onBtnClick
            )
        }
    }

    @MainThread
    fun showMultiStateErrorView(setup: (StateView.() -> Unit)?) {
        multiStateLayoutViewModel.showErrorView(setup)
    }

    @MainThread
    fun showMultiStateNoNetworkView() {
        multiStateLayoutViewModel.showNoNetworkView()
    }

    @MainThread
    fun showMultiStateNoNetworkView(setup: (StateView.() -> Unit)?) {
        multiStateLayoutViewModel.showNoNetworkView(setup)
    }

    suspend fun <T> requestApiWithProgressFaker(
        timeMax: Long = 6 * 1000,
        progressCallback: ProgressFakerTaskCallback,
        task: suspend () -> T
    ): T {
        return invokeTaskWithProgressFaker(
            timeMax,
            progressCallback = progressCallback,
            task = task
        )
    }

    suspend fun <T> requestApiWithProgressFaker(
        timeMax: Long = 6 * 1000,
        task: suspend () -> T
    ): T {
        return invokeTaskWithProgressFaker(
            timeMax = timeMax,
            progressCallback = MultiStateEventProcessFakerCallback(
                this@MultiStateViewModel.multiStateLayoutViewModel.stateLayoutActionEvent,
                timeMax
            ),
            task = task
        )
    }

//    class MultiStateProcessFakerCallback(
//        val vm: MultiStateViewModel,
//        val timeMax: Long,
//        val onlyProcessPercent: Boolean = false
//    ) : ProgressFakerTaskCallback {
//        override suspend fun onProgress(duration: Int, progress: Int) {
//            if (duration > timeMax / 2) {
//                vm.showMultiStateLoadingView("${if (onlyProcessPercent) "" else "拼命加载中..."}$progress%")
//            } else {
//                vm.showMultiStateLoadingView("${if (onlyProcessPercent) "" else "正在加载，请稍后..."}$progress%")
//            }
//        }
//    }

    class MultiStateEventProcessFakerCallback(
        val stateEvent: MutableLiveEvent<MultiStateLayoutViewModel.StateLayoutEventAction>,
        val timeMax: Long,
        val waitLongMsg: String = "拼命加载中...",
        val waitMsg: String = "正在加载，请稍后...",
    ) : ProgressFakerTaskCallback {
        override suspend fun onProgress(duration: Int, progress: Int) {
            stateEvent.value = MultiStateLayoutViewModel.StateLayoutEventAction {
                if (duration > timeMax / 2) {
                    it.showLoadingView {
                        showLoadingMsg("${waitLongMsg}$progress%")
                    }
                } else {
                    it.showLoadingView {
                        showLoadingMsg("${waitMsg}$progress%")
                    }
                }
            }

        }
    }

    class MultiStateLayoutProcessFakerCallback(
        val layout: StateLayout,
        val timeMax: Long,
        val waitLongMsg: String = "拼命加载中...",
        val waitMsg: String = "正在加载，请稍后...",
    ) : ProgressFakerTaskCallback {
        override suspend fun onProgress(duration: Int, progress: Int) {
            if (duration > timeMax / 2) {
                layout.showLoadingView {
                    showLoadingMsg("${waitLongMsg}$progress%")
                }
            } else {
                layout.showLoadingView {
                    showLoadingMsg("${waitMsg}$progress%")
                }
            }

        }
    }
}


