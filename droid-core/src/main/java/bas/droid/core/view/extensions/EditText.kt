package bas.droid.core.view.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

const val DEFAULT_SEARCH_THRESHOLD = 1000L

/**
 * 搜索用例场景
 *
 * @note 该方法主要用于展示[textChangedFlow]的使用。
 *
 * @param start 注意，该回调会在绑定事件时就触发了
 * @param scope 协程域
 * @param debounce 搜索防抖 即多少时间内只触发一次搜索
 * @param block 搜索业务逻辑
 * @param success 成功回调
 * @param error 异常回调
 */
fun <T> EditText.searchUseCase(
    scope: CoroutineScope,
    debounce: Long = DEFAULT_SEARCH_THRESHOLD,
    block: suspend (CharSequence?) -> T,
    start: FlowCollector<T>.() -> Unit,
    success: suspend (T) -> Unit,
    error: (Throwable) -> Unit,
    complete: () -> Unit
) {
    textChangedFlow()
        .debounce(debounce)
        .map(block)
        .flowOn(Dispatchers.IO)
        .onEach(success)
        .onStart(start)
        .onCompletion { e ->
            if (e != null) {
                error.invoke(e)
            } else {
                complete.invoke()
            }
        }
        .catch {
            // complete 已经分发了错误，所以不再这里分发，只是为了避免上流异常引起崩溃
        }
        .launchIn(scope)
}

fun EditText.textChangedFlow() = callbackFlow {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val result = trySend(p0)
            if (result.isSuccess) {
                //
                return
            }
            result.exceptionOrNull()?.printStackTrace()
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }
    addTextChangedListener(watcher)
    awaitClose {
        removeTextChangedListener(watcher)
    }
}