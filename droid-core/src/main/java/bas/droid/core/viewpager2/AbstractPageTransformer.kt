package bas.droid.core.viewpager2

import android.util.Log
import android.view.View
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * Created by Lucio on 2021/12/21.
 */
abstract class AbstractPageTransformer : ViewPager2.PageTransformer {

    protected fun log(msg: String) {
        Log.d("ScalePageTransformer", msg)
    }

    override fun transformPage(page: View, position: Float) {
        val viewPager = requireViewPager(page)
        val offscreenPageLimit = viewPager.offscreenPageLimit
        //当前行为的进度：中间位置 100% 两边位置 0%
        val percent: Float = 1.0f - abs(position) / offscreenPageLimit
        log("transformPage@${page.hashCode()}: position=${position} percent=${percent}")
        onTransformPage(viewPager, page, position, percent)
    }

    /**
     * @param viewPager 当前ViewPager2
     * @param page 对应[transformPage]中的参数
     * @param position 对应[transformPage]中的参数
     * @param percent 中间位置 = 1，两边（根据offscreenPageLimit确定两边，即ViewPager2左右两边的第一个元素）位置为0
     */
    abstract fun onTransformPage(
        viewPager: ViewPager2,
        page: View,
        position: Float,
        @FloatRange(from = 0.0, to = 1.0) percent: Float
    )

    protected fun requireViewPager(page: View): ViewPager2 {
        val parent = page.parent
        val parentParent = parent.parent
        if (parent is RecyclerView && parentParent is ViewPager2) {
            return parentParent
        }
        throw IllegalStateException(
            "Expected the page view to be managed by a ViewPager2 instance."
        )
    }
}