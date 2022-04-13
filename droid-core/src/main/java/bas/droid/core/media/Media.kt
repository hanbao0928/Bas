/**
 * Created by Lucio on 2021/6/18.
 */

@file:JvmName("MediasKt")
package bas.droid.core.media

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import java.io.File




/**
 * 创建视频缩略图
 */
fun createVideoThumbnail(videoPath: String): Bitmap? {
    if (!File(videoPath).exists()) throw IllegalStateException("file is not exists.")
    val media = MediaMetadataRetriever()
    media.setDataSource(videoPath)
    val bitmap = media.frameAtTime
    media.release()
    return bitmap
}

/**
 * Created by Lucio on 18/1/10.
 * 参考：http://developer.android.com/training/camera/photobasics.html
 * 国内访问地址：https://developer.android.google.cn/training/camera/photobasics.html
 */

/*在清单文件中添加如下配置，确保设备是否支持照相机才能下载应用
<manifest ... >
<uses-feature android:name="android.hardware.camera"
android:required="true" />
...
</manifest>*/


/**
 * 照相机是否可用
 */
fun Context.isCameraEnable(): Boolean {
    return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
}
