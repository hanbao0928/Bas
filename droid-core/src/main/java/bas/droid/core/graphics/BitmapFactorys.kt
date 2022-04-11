package bas.droid.core.graphics

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import kotlin.math.max

/**
 * 将[upBmp]居中覆盖在[downBmp]之上。
 * 如使用场景：二维码中间贴上logo
 * @param downBmp 下面的图片
 * @param upBmp 上面的图片
 * @param config 建议[Bitmap.Config.RGB_565],占用的内存更少
 * @return 混合之后的图片
 */
fun centerMixtureBitmap(
    downBmp: Bitmap,
    upBmp: Bitmap,
    config: Bitmap.Config = Bitmap.Config.RGB_565
): Bitmap {
    val dWidth = downBmp.width
    val dHeight = downBmp.height
    val uWidth = upBmp.width
    val uHeight = upBmp.height
    val width = max(dWidth, uWidth)
    val height = max(dHeight, uHeight)
    val newBmp = Bitmap.createBitmap(width, height, config)
    val cv = Canvas(newBmp)
    cv.drawBitmap(downBmp, 0f, 0f, null)
    cv.drawBitmap(upBmp, width / 2f, height / 2f, null)
    cv.save()
    cv.restore()
    return newBmp
}


/**
 * 缩放图片
 * @param src       用于缩放的bitmap对象
 * @param desWidth  所需要的宽
 * @param desHeight 所需要的高
 * @return
 */
fun zoomBitmap(src: Bitmap, desWidth: Int, desHeight: Int): Bitmap {
    val widthScale = desWidth.toFloat() / max(src.width, 1)
    val heightScale = desHeight.toFloat() / max(src.height, 1)
    return zoomBitmap(src, widthScale, heightScale)
}

/**
 * 缩放图片
 * @param src         用于缩放的bitmap对象
 * @param widthScale  宽的缩放比例
 * @param heightScale 高的缩放比列
 * @return
 */
fun zoomBitmap(
    src: Bitmap,
    @FloatRange(from = 0.0, to = 1.0) widthScale: Float,
    @FloatRange(from = 0.0, to = 1.0) heightScale: Float
): Bitmap {
    val matrix = Matrix()
    matrix.postScale(widthScale, heightScale)
    return Bitmap.createBitmap(
        src, 0, 0,
        (src.width * widthScale).toInt(),
        (src.height * heightScale).toInt(), matrix, true
    )
}

/**
 * 旋转bitmap
 *
 * @param bitmap
 * @param degree 旋转角度
 * @return
 */
fun rotateBitmap(bitmap: Bitmap, @IntRange(from = 0, to = 360) degree: Int): Bitmap {
    val matrix = Matrix().also {
        it.postRotate(degree.toFloat())
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
