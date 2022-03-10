package bas.droid.core.viewpager2

import android.view.View
import androidx.annotation.FloatRange
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by Lucio on 2021/12/21.
 * @param minAlpha 最小透明度值，0.0-1.0,0.0:全透明 1、不透明  相当于不使用alpha渐变效果
 */
class AlphaPageTransformer(@FloatRange(from = 0.0, to = 1.0) val minAlpha: Float) :
    AbstractPageTransformer() {

    private val alphaDiff: Float = 1.0f - minAlpha

    /**
     * @param percent 中间位置 = 1，两边（根据offscreenPageLimit确定两边，即ViewPager2左右两边的第一个元素）位置为0
     */
    override fun onTransformPage(
        viewPager: ViewPager2,
        page: View,
        position: Float,
        percent: Float
    ) {
        page.alpha = minAlpha + alphaDiff * percent
    }


}