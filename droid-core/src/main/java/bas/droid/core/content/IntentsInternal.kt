@file:JvmName("IntentsKt")
@file:JvmMultifileClass
package bas.droid.core.content

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build

/**
 * Created by Lucio on 2021/12/15.
 */

internal fun NotificationSettingDefault(ctx: Context): Intent {
    var intent = AppDetailSettingIntent(ctx.packageName)
    if (!intent.canResolved(ctx)) {
        intent = SysSettingIntent()
    }
    return intent
}


@TargetApi(26)
internal fun NotificationSettingApi26(ctx: Context): Intent {
    return Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .apply {
            putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, ctx.packageName)
            putExtra(
                android.provider.Settings.EXTRA_CHANNEL_ID,
                ctx.applicationInfo.uid
            )
        }
}

@TargetApi(19)
internal fun NotificationSettingApi19(ctx: Context): Intent {
    return Intent("android.settings.APP_NOTIFICATION_SETTINGS")
        .apply {
            putExtra("app_package", ctx.packageName)
            putExtra("app_uid", ctx.applicationInfo.uid)
        }
}


/**
 *  裁剪意图(用于从相册获取，不破坏原图)
 *
 * @param fromUri     数据源，被裁剪的图片URI
 * @param aspectX     X所占比例
 * @param aspectY     Y所占比例
 * @param outputX     X宽度
 * @param outputY     Y宽度
 * @param format 压缩格式，默认jpeg，占用内存更小
 */
internal fun CropIntentInternal(
    fromUri: Uri,
    aspectX: Int,
    aspectY: Int,
    outputX: Int,
    outputY: Int,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
): Intent {
    return Intent("com.android.camera.action.CROP").apply {
        if (Build.VERSION.SDK_INT >= 24) {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        setDataAndType(fromUri, "image/*")
        putExtra("crop", true)
        putExtra("aspectX", aspectX)
        putExtra("aspectY", aspectY)
        putExtra("outputX", outputX)
        putExtra("outputY", outputY)
        putExtra("scale", true)
        putExtra("outputFormat", format.toString())
        putExtra("noFaceDetection", true)
    }
}