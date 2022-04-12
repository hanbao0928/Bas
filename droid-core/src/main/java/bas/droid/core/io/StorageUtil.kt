@file:JvmName("StoragesKt")
@file:JvmMultifileClass

package bas.droid.core.io

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import bas.droid.core.util.getMimeType
import java.io.File


/**
 *  让系统媒体扫描器扫描指定路径的文件，确保生成的图片文件等能够被相册和其他应用发现使用
 *  场景：拍照之后，能够被其他应用QQ等选取发送
 *  invoke the system's media scanner to add your photo to the Media Provider's database,
 *  making it available in the Android Gallery application and to other apps.
 */
@Deprecated("maybe,it does not work on some devices")
fun Context.scanFileByBroadcast(path: String) {
    val f = File(path)
    if (f.exists())
        scanFileByBroadcast(Uri.fromFile(f))
}

/**
 *  让系统媒体扫描器扫描指定路径的文件，确保生成的图片文件等能够被相册和其他应用发现使用
 *  场景：拍照之后，能够被其他应用QQ等选取发送
 *  @see [scanFileByConnection]
 *  该方法通过广播的形式去扫描文件
 *  invoke the system's media scanner to add your photo to the Media Provider's database,
 *  making it available in the Android Gallery application and to other apps.
 */
@Deprecated("maybe,it does not work on some devices")
fun Context.scanFileByBroadcast(uri: Uri) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
    sendBroadcast(mediaScanIntent)
}

/**
 * 扫描指定媒体文件:通过[MediaScannerConnection]
 */
fun Context.scanFileByConnection(
    file: File,
    callback: MediaScannerConnection.OnScanCompletedListener?
) {
    val path = file.absolutePath
    val mimeType = file.getMimeType()
    scanFileByConnection(arrayOf(path), arrayOf(mimeType), callback)
}

/**
 * 扫描指定媒体文件:通过[MediaScannerConnection]
 */
fun Context.scanFileByConnection(
    path: String,
    callback: MediaScannerConnection.OnScanCompletedListener?
) {
    val mimeType = path.getMimeType()
    scanFileByConnection(arrayOf(path), arrayOf(mimeType), callback)
}

/**
 * 扫描指定媒体文件:通过[MediaScannerConnection]
 */
fun Context.scanFileByConnection(
    paths: Array<String>,
    mimeTypes: Array<String>?,
    callback: MediaScannerConnection.OnScanCompletedListener?
) {
    MediaScannerConnection.scanFile(this, paths, mimeTypes, callback)
}
