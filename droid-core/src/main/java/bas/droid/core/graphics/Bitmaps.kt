@file:JvmName("BitmapsKt")
@file:JvmMultifileClass

package bas.droid.core.graphics

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.media.ExifInterface
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.ColorInt
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.*

/**
 * 读取图片拍摄日期
 *
 * @param path
 */
fun readPictureTakeDate(path: String): Date? {
    var degree: Int
    val exifInterface = ExifInterface(path)
    val dateValue = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)
    return if (dateValue.isNullOrEmpty()) {
        null
    } else {
        try {
            DateFormat.getInstance().parse(path)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }
}

/**
 * 读取图片的旋转角度
 * @param path
 * @return 图片旋转的角度值（0,90,180,270）
 */
fun readPictureDegree(path: String): Int {
    return try {
        val exifInterface = ExifInterface(path)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_TRANSPOSE, ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> 180
            ExifInterface.ORIENTATION_TRANSVERSE, ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    } catch (e: Throwable) {
        0
    }
}

/**
 * 获取缩放之后的图片(是按照InSampleSize进行缩放，因此图片的宽度不会超过指定的[maxWidth]和[maxHeight],具体请了解InSampleSize的原理)
 * @param path 图片路径
 * @param maxWidth 需求宽
 * @param maxHeight 需求高
 */
@JvmOverloads
fun scaledBitmap(
    path: String,
    maxWidth: Int,
    maxHeight: Int
): Bitmap? {
    //创建用于缩放的Options
    val options = newBitmapOptionsForDecode(path, maxWidth, maxHeight)
    //解析出图片
    return decodeBitmap(path, options)
}

/**
 * 获取缩放压缩之后的图片数据
 * @see scaledBitmap
 *  * @param quality Hint to the compressor, 0-100. 0 meaning compress for
 *                 small size, 100 meaning compress for max quality. Some
 *                 formats, like PNG which is lossless, will ignore the
 *                 quality setting
 * @param format 压缩格式，默认jpeg
 */
@JvmOverloads
fun scaledCompressedBitmapData(
    path: String,
    reqWidth: Int,
    reqHeight: Int,
    quality: Int = 100,
    format: CompressFormat = CompressFormat.JPEG
): ByteArray {
    val bmp = scaledBitmap(path, reqWidth, reqHeight)
    requireNotNull(bmp) {
        "bitmap is null(or file not found),please check your path is right."
    }
    val results = bmp.toByteArray(format, quality)
    bmp.recycle()
    return results
}

/**
 * 获取缩放压缩、角度修正之后的图片数据
 * @param path
 * @return
 */
@JvmOverloads
fun correctedScaledCompressedBitmapData(
    path: String,
    maxWidth: Int,
    maxHeight: Int,
    quality: Int = DEFAULT_BITMAP_COMPRESS_QUALITY,
    format: CompressFormat = CompressFormat.JPEG
): ByteArray {
    //获取偏转角度
    val degree = readPictureDegree(path)
    if (degree == 0) {
        return scaledCompressedBitmapData(path, maxWidth, maxHeight, quality)
    } else {
        //创建用于缩放的Options
        val options = newBitmapOptionsForDecode(path, maxWidth, maxHeight)
        //解析出图片
        val bitmap = decodeBitmap(path, options)
        requireNotNull(bitmap) {
            "bitmap is null,please check your path is right."
        }
        //旋转图片
        val rotatedBmp = rotateBitmap(bitmap, degree)
        bitmap.recycle()
        val results = rotatedBmp.toByteArray(format, quality)
        rotatedBmp.recycle()
        return results
    }
}


/**
 * 获取图片的BitmapFactory.Options
 */
fun newBitmapOptionsForDecode(ctx: Context, imageUri: Uri): BitmapFactory.Options {
    val optionsInfo = BitmapFactory.Options()
    // 这里设置true的时候，decode时候Bitmap返回的为空，
    // 将图片宽高读取放在Options里.
    optionsInfo.inJustDecodeBounds = true
    decodeBitmap(ctx, imageUri, optionsInfo)
    return optionsInfo
}


/**
 * 生成文字图片
 * @param texString 文本内容
 * @param textSize 文本大小
 * @param textColor 文本颜色
 */
fun TextBitmap(
    texString: String,
    textSize: Float,
    @ColorInt textColor: Int
): Bitmap {
    val paint = TextPaint()
    paint.isAntiAlias = true
    paint.textSize = textSize
    paint.color = textColor

    val fm = paint.fontMetrics
    // 获取文本宽高
    val textHeight = Math.ceil((fm.descent - fm.ascent).toDouble()).toInt() + 2
    val textWidth = paint.measureText(texString).toInt() + 6
    return TextBitmap(textWidth, textHeight, texString, paint)
}

/**
 * 生成文字图片
 * @param width 图片宽度
 * @param height 图片高度
 * @param text 图片内容
 * @param paint 文字画笔
 * @param config 图片设置
 */
@JvmOverloads
fun TextBitmap(
    width: Int,
    height: Int,
    text: String,
    paint: TextPaint,
    config: Bitmap.Config = Bitmap.Config.ARGB_4444
): Bitmap {
    val newBitmap = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(newBitmap)
    val sl = StaticLayout(
        text, paint, newBitmap.width,
        Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false
    )
    sl.draw(canvas)
    return newBitmap
}

/**
 * 保存为文件
 *
 * @param path                    文件路径
 * @param format                  压缩格式
 * @param quality                 压缩质量
 * @param recreateIfExits 文件存在时，是否删除重建
 * @return true:保存成功
 */
@JvmOverloads
fun Bitmap.saveToFile(
    path: String,
    format: CompressFormat = CompressFormat.JPEG,
    quality: Int = 100, recreateIfExits: Boolean = false
): Boolean {

    val file = File(path)
    //删除原有文件
    if (file.exists()) {
        if (recreateIfExits) {
            //删除原有文件
            file.delete()
        } else {
            return true
        }
    }

    val prentFile = file.parentFile
    if (prentFile != null && !prentFile.exists()) {
        prentFile.mkdirs()
    }
    file.createNewFile()

    return try {
        val out = FileOutputStream(file)
        compress(format, quality, out)
        out.flush()
        out.close()
        true
    } catch (e: Throwable) {
        if (file.exists())
            file.deleteOnExit()
        false
    }

}

