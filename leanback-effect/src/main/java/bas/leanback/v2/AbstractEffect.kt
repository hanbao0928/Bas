package bas.leanback.v2

import android.graphics.Canvas
import bas.leanback.effect.EffectParams

abstract class AbstractEffect(val params: EffectParams) {

    abstract fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int)

    abstract fun onDraw(canvas: Canvas)

}