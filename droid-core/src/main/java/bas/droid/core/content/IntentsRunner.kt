/**
 * Created by Lucio on 18/2/1.
 */

@file:JvmName("IntentsKt")
@file:JvmMultifileClass

package bas.droid.core.content

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.Nullable
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment

/**
 * 回到系统主界面
 */
fun Context.startIntentForMainHome() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    startActivity(intent)
}

/**
 * 寻求打开网页的Intent
 */
fun Context.startIntentForUrl(url: String) {
    val uri = Uri.parse(url)
    startIntentForUri(uri)
}

/**
 * 使用系统浏览器打开网页
 */
fun Context.startIntentForUrlBySystemBrowser(url: String) {
    val uri = Uri.parse(url)
    try {
        val it = Intent(Intent.ACTION_VIEW)
        it.data = uri
        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity")
        startActivity(it)
    } catch (e: Exception) {
        e.printStackTrace()
        startIntentForUri(uri)
    }
}

/**
 * 寻求打开对应数据的Intent
 */
fun Context.startIntentForUri(uri: Uri) {
    val it = Intent(Intent.ACTION_VIEW)
    it.data = uri
    startActivity(it)
}

/**
 * 调用拨号界面
 * @param tel 电话号码（可不传）
 */
@JvmOverloads
fun Context.startIntentForDial(@Nullable tel: String? = null) {
    startActivity(DialIntent(tel))
}

/**
 * 拨打电话
 * @param tel 电话好吗
 * @RequiresPermission 需要危险权限[Manifest.permission.CALL_PHONE]
 */
@RequiresPermission(value = Manifest.permission.CALL_PHONE)
fun Context.startIntentForCall(tel: String) {
    startActivity(CallIntent(tel))
}

/**
 * 调用短信编辑界面
 * @param context
 * @param tel          电话号码
 * @param extraContent 预设的短信内容
 */
fun Context.startIntentForSMS(tel: String, extraContent: String = "") {
    startActivity(SMSIntent(tel, extraContent))
}

/**
 * 发邮件
 * @param addrs   邮箱地址数组
 * @param subject      邮件主题
 * @param extraContent 预设的邮件内容
 */
@JvmOverloads
fun Context.startIntentForMail(
    addrs: Array<String>,
    chooserTitle: String = "发送邮件",
    subject: String = "",
    extraContent: String = ""
) {
    startActivity(MailIntent(addrs, chooserTitle, subject, extraContent))
}

/**
 * 调用手机设置界面
 * 具体设置见 [android.provider.Settings]的Action_xxxx定义
 */
fun Context.startIntentForSettings() {
    startIntentForAction(android.provider.Settings.ACTION_SETTINGS)
}


/**
 * 查看应用设置信息
 */
fun Context.startIntentForAppDetailSetting(pkgName: String) {
    val uri = Uri.fromParts("package", pkgName, null)
    startIntentForAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
}

/**
 * 运行到具体设置界面
 * @param action
 * @see [android.provider.Settings]的Action_xxxx定义
 */
private inline fun Context.startIntentForAction(action: String, data: Uri? = null) {
    val it = Intent(action)
    if (data != null) {
        it.data = data
    }
    startActivity(it)
}

/**
 * 拍照意图（不返回数据，通过URI获取）
 * 在[Activity.onActivityResult]方法中操作此方法的Uri参数，即可处理数据
 * @param outputUri         用于存储拍照之后的图片
 */
fun Activity.startIntentForPhotograph(outputUri: Uri, requestCode: Int) {
    startActivityForResult(PhotographIntent(outputUri),requestCode)
}

/**
 * 拍照意图（不返回数据，通过URI获取）
 * 在[Fragment.onActivityResult]方法中操作此方法的Uri参数，即可处理数据
 * @param outputUri         用于存储拍照之后的图片
 */
fun Fragment.startIntentForPhotograph(outputUri: Uri, requestCode: Int) {
    startActivityForResult(PhotographIntent(outputUri),requestCode)
}