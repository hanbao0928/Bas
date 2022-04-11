@file:JvmName("BitmapsKt")
@file:JvmMultifileClass

package bas.droid.core.graphics

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.ColorInt
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.*
import kotlin.math.max

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
        } catch (e: Exception) {
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
    val exifInterface = ExifInterface(path)
    val orientation = exifInterface.getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_TRANSPOSE, ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> 180
        ExifInterface.ORIENTATION_TRANSVERSE, ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
}

/**
 * 获取缩放之后的图片(是按照InSampleSize进行缩放)
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
    val options = BitmapOptions(path, maxWidth, maxHeight)
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
        "bitmap is null,please check your path is right."
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
fun correctedScaledCompressedBitmapData(
    path: String,
    maxWidth: Int,
    maxHeight: Int,
    quality: Int = 100,
    format: CompressFormat = CompressFormat.JPEG
): ByteArray {
    //获取偏转角度
    val degree = ImageRotateDegree(path)
    if (degree == 0) {
        return scaledCompressedBitmapData(path, maxWidth, maxHeight, quality)
    } else {
        //创建用于缩放的Options
        val options = BitmapOptions(path, maxWidth, maxHeight)
        //解析出图片
        val bitmap = decodeBitmap(path, options)
        requireNotNull(bitmap) {
            "bitmap is null,please check your path is right."
        }
        val fixedBmp = rotateBitmap(bitmap, degree)
        bitmap.recycle()
        val results = fixedBmp.toByteArray(format, quality)
        fixedBmp.recycle()
        return results
    }
}

/**
 * 通过需求的宽和高简单计算适当的InSampleSize
 *
 * @param imgWidth 图片宽
 * @param imgHeight 图片高
 * @param maxWidth  需求宽
 * @param maxHeight 需求高
 * @return
 */
fun InSampleSizeSimple(imgWidth: Int, imgHeight: Int, maxWidth: Int, maxHeight: Int): Int {
    val calResult = max(imgWidth * 1.0 / maxWidth, imgHeight * 1.0 / maxHeight).toInt()
    return max(1, calResult)
}

/**
 * 解析Bitmap的公用方法. 数据源只需提供一种
 */
fun decodeBitmap(path: String, options: BitmapFactory.Options): Bitmap? {
    return BitmapFactory.decodeFile(path, options)
}

/**
 * 获取缩放之后的图片
 * @param maxWidth 目标宽度
 * @param maxHeight 目标高度
 * @param imagePath 图片路径
 * @param config 压缩配置，默认[Bitmap.Config.RGB_565],所占内存最少
 */
fun decodeBitmap(
    imagePath: String,
    maxWidth: Int,
    maxHeight: Int,
    config: Bitmap.Config = Bitmap.Config.RGB_565
): Bitmap? {
    val bmOptions = BitmapOptions(imagePath, maxWidth, maxHeight, config)
    return decodeBitmap(imagePath, bmOptions)
}

/**
 * 解析Bitmap的公用方法. 数据源只需提供一种
 */
fun decodeBitmap(data: ByteArray, options: BitmapFactory.Options): Bitmap? {
    return BitmapFactory.decodeByteArray(data, 0, data.size, options)
}

/**
 * 解析Bitmap的公用方法. 数据源只需提供一种
 */
fun decodeBitmap(ctx: Context, uri: Uri, options: BitmapFactory.Options): Bitmap? {
    return ctx.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, options)
    }
}

/**
 * 获取图片的BitmapFactory.Options
 */
fun BitmapOptions(path: String): BitmapFactory.Options {
    val optionsInfo = BitmapFactory.Options()
    // 这里设置true的时候，decode时候Bitmap返回的为空，
    // 将图片宽高读取放在Options里.
    optionsInfo.inJustDecodeBounds = true
    decodeBitmap(path, optionsInfo)
    return optionsInfo
}

/**
 * 获取图片的BitmapFactory.Options
 */
fun BitmapOptions(data: ByteArray): BitmapFactory.Options {
    val optionsInfo = BitmapFactory.Options()
    // 这里设置true的时候，decode时候Bitmap返回的为空，
    // 将图片宽高读取放在Options里.
    optionsInfo.inJustDecodeBounds = true
    decodeBitmap(data, optionsInfo)
    return optionsInfo
}

/**
 * 获取图片的BitmapFactory.Options
 */
fun BitmapOptions(ctx: Context, imageUri: Uri): BitmapFactory.Options {
    val optionsInfo = BitmapFactory.Options()
    // 这里设置true的时候，decode时候Bitmap返回的为空，
    // 将图片宽高读取放在Options里.
    optionsInfo.inJustDecodeBounds = true
    decodeBitmap(ctx, imageUri, optionsInfo)
    return optionsInfo
}

/**
 * 创建一个合适的用于获取图片的BitmapFactory.Options
 * @param path
 * @param maxWidth
 * @param maxHeight
 * @param jpegQuality
 * @return
 */
fun BitmapOptions(
    path: String,
    maxWidth: Int,
    maxHeight: Int,
    config: Bitmap.Config = Bitmap.Config.ARGB_8888
): BitmapFactory.Options {
    val options = BitmapOptions(path)
    options.inSampleSize = InSampleSizeSimple(options.outWidth, options.outHeight, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    if (Build.VERSION.SDK_INT < 21) {
        options.inPurgeable = true
        options.inInputShareable = true
    }
    return options
}

/**
 * 获取图片的旋转角度
 * @param path 图片路径
 * @return 图片旋转的角度值（0,90,180,270）
 */
fun ImageRotateDegree(path: String): Int {
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
    } catch (e: Exception) {
        0
    }
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
    } catch (e: Exception) {
        if (file.exists())
            file.deleteOnExit()
        false
    }

}

