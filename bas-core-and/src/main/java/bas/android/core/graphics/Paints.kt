package bas.android.core.graphics

import android.graphics.Paint

/**
 * Created by Lucio on 2021/12/11.
 */

val Paint.baseline:Float get() {
    val fontMetrics = this.fontMetrics
    return (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
}

