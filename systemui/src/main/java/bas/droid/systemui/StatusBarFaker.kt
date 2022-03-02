package bas.droid.systemui

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

/**
 * Created by Lucio on 2019/6/6.
 * 状态栏伪装（与状态栏等高的View，用于需要放在布局顶端占用状态栏高度的场景）
 */
class StatusBarFaker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureWidth(measureSpec: Int): Int {
        return getMeasureSize(
            resources.displayMetrics.widthPixels.coerceAtLeast(suggestedMinimumWidth),
            measureSpec,
            "measureWidth"
        )
    }

    private fun measureHeight(measureSpec: Int): Int {
        return if (Build.VERSION.SDK_INT < 19) {
            //19之前，无法fake状态栏
            0
        } else {
            getMeasureSize(
                getAvailableStatusBarHeight(context).coerceAtLeast(suggestedMinimumHeight),
                measureSpec,
                "measureHeight"
            )
        }
    }

    private fun getMeasureSize(size: Int, measureSpec: Int, tag: String): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
            log("$tag:[EXACTLY] width=$result")
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = min(size, specSize)
            log("$tag:[AT_MOST] width=$result")
        } else {
            result = size
            log("$tag:[UNSPECIFIED] width=$result")
        }
        return result
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }

    companion object {

        private const val TAG = "FakeStatusBar"

        @JvmStatic
        private var availableStatusBarHeight = 0

        /**
         * 获取状态栏高度;通过资源读取状态栏高度
         */
        @JvmStatic
        fun getStatusBarHeight(ctx: Context): Int {
            return ctx.applicationContext.resources.run {
                var result = 0
                try {
                    val resourceId = this.getIdentifier("status_bar_height", "dimen", "android")
                    if (resourceId > 0) {
                        result = this.getDimensionPixelSize(resourceId)
                    }
                } catch (e: Resources.NotFoundException) {
                }
                result
            }
        }

        @JvmStatic
        fun getAvailableStatusBarHeight(ctx: Context): Int {
            if (availableStatusBarHeight == 0) {
                availableStatusBarHeight = getStatusBarHeight(ctx)
                if (availableStatusBarHeight == 0 && Build.VERSION.SDK_INT >= 19) {
                    //如果获取系统状态栏高度失败，并且设备系统大于19，则默认25dp
                    availableStatusBarHeight =
                        (ctx.applicationContext.resources.displayMetrics.density * 25).toInt()
                }
            }
            return availableStatusBarHeight
        }

    }
}