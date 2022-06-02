package bas.droid.arch


sealed interface UiState {

    val extra: Any?

    /**
     * loading状态
     */
    data class LoadingUiState @JvmOverloads constructor(
        val message: String,
        override val extra: Any? = null
    ) : UiState

    /**
     * 数据状态
     */
    data class ContentUiState<T> @JvmOverloads constructor(
        val data: T,
        override val extra: Any? = null
    ) : UiState

    /**
     * 错误状态
     */
    data class ErrorUiState @JvmOverloads constructor(
        val error: Throwable,
        val message: String = error.message.orEmpty(),
        override val extra: Any? = null
    ) : UiState

//    data class MessageUiState(
//        val id: Long = UUID.randomUUID().mostSignificantBits,
//        val message: String,
//        override val extra: Any? = null
//    ) : UiState

    /**
     * 懒惰状态:不知道该干嘛，，保持现状？
     */
    object LAZY : UiState {
        override val extra: Any? = null
    }
}