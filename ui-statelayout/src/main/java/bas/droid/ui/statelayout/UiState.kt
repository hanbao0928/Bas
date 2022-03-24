package bas.droid.ui.statelayout

/**
 * Created by Lucio on 2022/3/23.
 */

sealed class UiState(val extra: Any? = null) {

    class LoadingState(val message: String, extra: Any? = null) : UiState(extra)

    class EmptyState(val message: String, extra: Any? = null) : UiState(extra)

    class ErrorState(val error: Throwable, extra: Any? = null) : UiState(extra)

    class ContentState(extra: Any? = null) : UiState(extra)


}