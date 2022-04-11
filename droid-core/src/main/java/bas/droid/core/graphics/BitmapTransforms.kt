@file:JvmName("BitmapsKt")
@file:JvmMultifileClass

package bas.droid.core.graphics

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.IntRange
import bas.lib.core.lang.toBase64EncodeString
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.math.min

const val DEFAULT_BITMAP_COMPRESS_QUALITY = 80

/**
 * 转换成圆形bitmap
 * @return
 */
fun Bitmap.toCircleBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    val height = this.height
    val width = this.width
    val size = min(height, width)
    val output = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, width, height)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = Color.TRANSPARENT
    canvas.drawCircle((width / 2f), (height / 2f), (size / 2f), paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return output
}

fun Drawable.toBitmap(): Bitmap? {
    return (this as? BitmapDrawable)?.bitmap
}

/**
 * 字节数组转换成Bitmap
 */
fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

/**
 * Create drawable from a bitmap, not dealing with density.
 * @deprecated Use {@link #BitmapDrawable(Resources, Bitmap)} to ensure
 * that the drawable has correctly set its target density.
 */
@Deprecated(message = "Use toDrawable(Resources)")
fun Bitmap.toDrawable(): BitmapDrawable {
    return BitmapDrawable(this)
}

/**
 * 转换成Drawable
 */
fun Bitmap.toDrawable(res: Resources): BitmapDrawable {
    return BitmapDrawable(res, this)
}

/**
 * 转换成字节数组
 * @param compressFormat      转换格式，默认JPEG，这样转换之后的图片不包含透明像素，所占空间会更小（如果jpeg图片使用PNG格式转换，会导致转换之后的空间更大）
 * @param quality     转换质量（0-100取值），默认80
 * @return
 */
@JvmOverloads
fun Bitmap.toByteArray(
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    @IntRange(from = 0, to = 100) quality: Int = DEFAULT_BITMAP_COMPRESS_QUALITY
): ByteArray {
    var output: ByteArrayOutputStream? = null
    val result: ByteArray
    try {
        output = ByteArrayOutputStream()
        this.compress(compressFormat, quality, output)
        result = output.toByteArray()
    } finally {
        output?.close()
    }
    return result
}

/**
 * 把bitmap转换成Base64编码String
 *
 * @param compressFormat      转换格式，默认JPEG，这样转换之后的图片不包含透明像素，所占空间会更小（如果jpeg图片使用PNG格式转换，会导致转换之后的空间更大）
 * @param quality     转换质量（0-100取值），默认80
 * @return
 */
fun Bitmap.toBase64String(
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    @IntRange(from = 0, to = 100) quality: Int = DEFAULT_BITMAP_COMPRESS_QUALITY
): String {
    return this.toByteArray(compressFormat, quality).toBase64EncodeString()
}

/**
 * 获取bitmap的InputStream
 *
 * @param bm
 * @return
 */
fun Bitmap.toInputStream(
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    @IntRange(from = 0, to = 100) quality: Int = 100
): InputStream {
    val baos = ByteArrayOutputStream()
    this.compress(compressFormat, 100, baos)
    return ByteArrayInputStream(baos.toByteArray())
}


/**
 * 转换成字节数组
 * @param recycleSelf 是否需要在转换之后回收bitmap,如果bitmap在转换之后不需要再使用，则建议设置为true
 * @param compressFormat      转换格式，默认JPEG，这样转换之后的图片不包含透明像素，所占空间会更小（如果jpeg图片使用PNG格式转换，会导致转换之后的空间更大）
 * @param quality     转换质量（0-100取值），默认80
 * @return
 */

@Deprecated("使用另外一个函数好些，这里面提供了recycler source bitmap的功能，感觉这部分应该由用户自身去处理")
fun Bitmap.toByteArray(
    recycleSelf: Boolean = false,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    @IntRange(from = 0, to = 100) quality: Int = DEFAULT_BITMAP_COMPRESS_QUALITY
): ByteArray? {
    var output: ByteArrayOutputStream? = null
    val result: ByteArray
    try {
        output = ByteArrayOutputStream()
        this.compress(compressFormat, quality, output)
        if (recycleSelf) {
            recycle()
        }
        result = output.toByteArray()
    } finally {
        output?.close()
    }
    return result
}