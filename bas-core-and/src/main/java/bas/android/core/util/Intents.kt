package bas.android.core.util

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import com.bas.core.lang.onCatch
import com.bas.core.lang.tryIgnore

/**
 * Created by Lucio on 2021/10/27.
 */

inline fun Intent.checkValidationOrThrow(ctx: Context) {
    resolveActivity(ctx.packageManager)
        ?: throw ActivityNotFoundException("no activity can handle this intent $this")
}

inline fun Intent.canResolve(ctx: Context):Boolean {
    return resolveActivity(ctx.packageManager) !=null
}

fun Context.startActivitySafely(intent: Intent){
    tryIgnore { 
        intent.checkValidationOrThrow(this)
        if(this !is Activity){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        this.startActivity(intent)
    }.onCatch {
        Log.w(this::class.java.simpleName,"无法打开指定Intent",it)
    }
}

fun Activity.startActivityForResultSafely(intent: Intent,requestCode: Int){
    tryIgnore {
        intent.checkValidationOrThrow(this)
        this.startActivityForResult(intent,requestCode)
    }.onCatch {
        Log.w(this::class.java.simpleName,"无法打开指定Intent",it)
    }
}

fun Fragment.startActivitySafely(intent: Intent) {
    tryIgnore {
        intent.checkValidationOrThrow(this.requireContext())
        this.startActivity(intent)
    }.onCatch {
        Log.w(this::class.java.simpleName,"无法打开指定Intent",it)
    }
}

fun Fragment.startActivityForResultSafely(intent: Intent, requestCode: Int) {
    tryIgnore {
        intent.checkValidationOrThrow(this.requireContext())
        this.startActivityForResult(intent,requestCode)
    }.onCatch {
        Log.w(this::class.java.simpleName,"无法打开指定Intent",it)
    }
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

private fun NotificationSettingDefault(ctx: Context): Intent {
    var intent =
        AMIntents.Setting.createAppDetailSettingIntent(ctx.packageName)
    if (intent.resolveActivity(ctx.packageManager) == null) {
        intent = AMIntents.Setting.createSettingIntent()
    }
    return intent
}

@TargetApi(26)
private fun NotificationSettingApi26(ctx: Context): Intent {
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
private fun NotificationSettingApi19(ctx: Context): Intent {
    return Intent("android.settings.APP_NOTIFICATION_SETTINGS")
        .apply {
            putExtra("app_package", ctx.packageName)
            putExtra("app_uid", ctx.applicationInfo.uid)
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