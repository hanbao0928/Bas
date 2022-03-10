package bas.droid.core.viewpager2

import android.view.View
import androidx.annotation.FloatRange
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by Lucio on 2021/12/21.
 * @param minScale
 */
class ScalePageTransformer(@FloatRange(from = 0.0, to = 1.0) val minScale: Float) :
    AbstractPageTransformer() {

    private val scaleValue: Float = 1.0f - minScale

    /**
     * @param percent 中间位置 = 1，两边（根据offscreenPageLimit确定两边，即ViewPager2左右两边的第一个元素）位置为0
     */
    override fun onTransformPage(
        viewPager: ViewPager2,
        page: View,
        position: Float,
        percent: Float
    ) {
        val scale = minScale + scaleValue * percent
        if (viewPager.orientation == ViewPager2.ORIENTATION_VERTICAL) {
            page.pivotX = page.width / 2f
            //即缩放是从上边/下边边向中间缩放
            page.pivotY = if (position < 0) 0f else page.height.toFloat()
        } else {
            //即缩放是从左边/右边向中间缩放
            page.pivotX = if (position < 0) 0f else page.width.toFloat()
            page.pivotY = page.height / 2f
        }
        page.scaleX = scale
        page.scaleY = scale
    }


}