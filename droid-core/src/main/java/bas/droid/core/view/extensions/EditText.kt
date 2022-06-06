package bas.droid.core.view.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

/**
 * 搜索用例场景
 * @param
 * @param debounce 搜索防抖 即多少时间内只触发一次搜索
 * @param block 搜索业务逻辑
 *
 */
fun <T> EditText.searchUseCase(
    scope: CoroutineScope,
    debounce: Long = 300,
    block: suspend (CharSequence?) -> T,
    success: suspend (T) -> Unit,
    error: suspend (Throwable) -> Unit
) {
    textChangedFlow()
        .debounce(debounce)
        .map(block)
        .flowOn(Dispatchers.IO)
        .onEach(success)
        .single()

}

fun EditText.textChangedFlow() = callbackFlow<CharSequence?> {
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