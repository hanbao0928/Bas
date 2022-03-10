@file:JvmName("Intents")
@file:JvmMultifileClass

package bas.droid.core.util

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.Nullable
import androidx.annotation.RequiresPermission

/**
 * Created by Lucio on 2021/10/27.
 */

inline fun Intent.checkValidationOrThrow(ctx: Context) {
    resolveActivity(ctx.packageManager)
        ?: throw ActivityNotFoundException("no activity can handle this intent $this")
}

inline fun Intent.canResolve(ctx: Context): Boolean {
    return resolveActivity(ctx.packageManager) != null
}

/**
 * 拍照意图（不返回数据，通过URI获取）
 * 在[Activity.onActivityResult]方法中操作此方法的Uri参数，即可处理数据
 * @param outputUri         用于存储拍照之后的图片
 */
fun PhotographIntent(outputUri: Uri): Intent {
    return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        if (Build.VERSION.SDK_INT >= 24) {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}

/**
 * 图片选择意图（不返回数据，通过在[Activity.onActivityResult]方法中调用data.getData()获取返回的数据）
 */
@JvmOverloads
fun PhotoPickIntent(mimeType: String = "image/*"): Intent {
    return Intent(Intent.ACTION_PICK)
        .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mimeType)
}

/**
 * 裁剪意图(不通过onActivityResult返回结果，结果保存在[toUri]中)
 * 适用场景：适用任意场景，对于所需图片尺寸较大的结果的情况则必须适用此方法，否则在某些机型上可能崩溃
 * @param fromUri     数据源，被裁剪的图片URI
 * @param toUri 保存结果的uri
 * @param aspectX     X所占比例
 * @param aspectY     Y所占比例
 * @param outputX     X宽度
 * @param outputY     Y宽度
 * @param format 压缩格式，默认jpeg，占用内存更小
 */
fun CropImageIntent(
    fromUri: Uri,
    toUri: Uri,
    aspectX: Int,
    aspectY: Int,
    outputX: Int,
    outputY: Int,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
): Intent {
    return CropIntentInternal(
        fromUri,
        aspectX,
        aspectY,
        outputX,
        outputY,
        format
    ).putExtra(MediaStore.EXTRA_OUTPUT, toUri)
        .putExtra("return-data", false)
}

/**
 * 裁剪意图(直接返回裁剪后的Bitmap数据)
 * 【返回数据获取】onActivityResult：（Bitmap）data.getExtras().getParcelable("data")）
 * 适用场景：类似于头像获取这种所需的结果尺寸较小的情况
 * @param fromUri     数据源，被裁剪的图片URI
 * @param aspectX     X所占比例
 * @param aspectY     Y所占比例
 * @param outputX     X宽度
 * @param outputY     Y宽度
 * @param format 压缩格式，默认jpeg，占用内存更小
 */
fun CropImageIntent(
    fromUri: Uri,
    aspectX: Int,
    aspectY: Int,
    outputX: Int,
    outputY: Int,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
): Intent {
    return CropIntentInternal(
        fromUri,
        aspectX,
        aspectY,
        outputX,
        outputY,
        format
    ).putExtra("return-data", true)
}


/**
 * 拨号意图（只是唤起电话输入界面）
 * @param tel 电话号码（可不传）
 */
@JvmOverloads
fun DialIntent(@Nullable tel: String? = null): Intent {
    return Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel.orEmpty()))
}

/**
 * 拨打电话意图
 * @param tel 电话好吗
 * @RequiresPermission 需要危险权限[Manifest.permission.CALL_PHONE]
 */
@RequiresPermission(value = Manifest.permission.CALL_PHONE)
fun CallIntent(tel: String): Intent {
    return Intent(Intent.ACTION_CALL, Uri.parse("tel:$tel"))
}

/**
 * 短信编辑意图
 * @param tel          电话号码
 * @param extraContent 预设的短信内容
 */
@JvmOverloads
fun SMSIntent(tel: String, extraContent: String = ""): Intent {
    return Intent(Intent.ACTION_VIEW)
        .putExtra("address", tel)
        .putExtra("sms_body", extraContent)
        .setType("vnd.android-dir/mms-sms")
}

/**
 * 发邮件意图
 * @param addrs   邮箱地址数组
 * @param subject      邮件主题
 * @param extraContent 预设的邮件内容
 */
@JvmOverloads
fun MailIntent(
    addrs: Array<String>,
    subject: String? = null,
    extraContent: String? = null
): Intent {
    val it = Intent(Intent.ACTION_SEND)
    // 设置对方邮件地址
    it.putExtra(Intent.EXTRA_EMAIL, addrs)
    // 设置标题内容
    if (!subject.isNullOrEmpty())
        it.putExtra(Intent.EXTRA_SUBJECT, subject)
    // 设置邮件文本内容
    if (!extraContent.isNullOrEmpty())
        it.putExtra(Intent.EXTRA_TEXT, extraContent)

    return it
}

/**
 * 系统设置界面
 * 具体设置见 [android.provider.Settings]的Action_xxxx定义
 */
fun SysSettingIntent(): Intent {
    return Intent(android.provider.Settings.ACTION_SETTINGS)
}

/**
 * 网络设置意图
 */
@Throws(ActivityNotFoundException::class)
fun WirelessSettingIntent(): Intent = Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)

/**
 * wifi设置意图
 */
@Throws(ActivityNotFoundException::class)
fun WifiSettingIntent(): Intent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)

/**
 * 通知设置界面意图
 */
fun NotificationSettingIntent(ctx: Context): Intent {
    val sdkInt = Build.VERSION.SDK_INT
    return when {
        sdkInt >= 26 -> NotificationSettingApi26(
            ctx
        )
        sdkInt >= 19 -> NotificationSettingApi19(
            ctx
        )
        else -> NotificationSettingDefault(ctx)
    }
}


/**
 * 应用详情设置界面意图
 */
fun AppDetailSettingIntent(pkgName: String): Intent {
    return Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", pkgName, null)
    }
}

/**
 * 系统浏览器意图
 */
fun SysBrowserIntent(url: String): Intent {
    val it = BrowserIntent(url)
    it.setClassName("com.android.browser", "com.android.browser.BrowserActivity")
    return it
}

/**
 * 超链接意图
 */
fun BrowserIntent(url: String): Intent {
    return ActionIntent(Uri.parse(url))
}

/**
 * 从系统选择内容意图
 * 场景：选择文件等
 * @param type 内容类型
 */
fun GetContentIntent(type: String = "*/*"): Intent {
    val intent: Intent?
    if (Build.VERSION.SDK_INT < 19) {
        intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = type
        intent.addCategory(Intent.CATEGORY_OPENABLE)
    } else {
        intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = type
    }
    return intent
}

/**
 * 系统主界面
 */
fun SysHomeIntent(): Intent {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    return intent
}

/**
 * 请求系统处理对应URI的意图
 */
inline fun ActionIntent(uri: Uri): Intent {
    return Intent(Intent.ACTION_VIEW, uri)
}

/**
 * 请求系统能够处理[data]的意图
 */
inline fun ActionIntent(data: String): Intent {
    return ActionIntent(Uri.parse(data))
}

/**
 * 指定包名启动意图
 */
fun LauncherIntent(ctx: Context, pkgName: String): Intent? {
    return ctx.packageManager.getLaunchIntentForPackage(pkgName)
}

/**
 * 指定包名和组件名启动意图
 */
fun LauncherIntent(pkgName: String, clsName: String): Intent {
    return Intent().apply {
        component = ComponentName(pkgName, clsName)
    }
}

/**
 * 应用市场意图
 */
fun AppStoreIntent(ctx: Context): Intent {
    return AppStoreIntent(ctx.packageName)
}

/**
 * 应用市场意图
 * @param pkgName 应用包名
 */
fun AppStoreIntent(pkgName: String): Intent {
    val uri = Uri.parse("market://details?id=$pkgName")
    return Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}