/**
 * Created by Lucio on 2020-11-02.
 * 对话框帮助类
 */
package bas.droid.core.ui

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import bas.lib.core.BasConfigurator


/**
 * 设置默认的对话框需求处理类
 */
fun BasConfigurator.setDialogUIHandler(handler: DialogUI) {
    dialogUI = handler
}


var dialogUI: DialogUI = DefaultDialogUI
    private set

interface DialogUI {

    fun showLoading(ctx: Context, message: CharSequence): ProgressDialog

    fun showAlertDialog(
        ctx: Context,
        @StringRes messageId: Int,
        @StringRes positiveTextId: Int
    ): Dialog {
        return showAlertDialog(ctx, ctx.getText(messageId), ctx.getText(positiveTextId))
    }

    fun showAlertDialog(ctx: Context, message: CharSequence, positiveBtnText: CharSequence): Dialog

    fun showAlertDialog(
        ctx: Context,
        message: CharSequence,
        okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
        cancelable: Boolean
    ): Dialog {
        return showAlertDialog(ctx, message, okPair, null, cancelable)
    }

    fun showAlertDialog(
        ctx: Context,
        @StringRes messageId: Int,
        okPair: Pair<Int, DialogInterface.OnClickListener?>,
        cancelable: Boolean
    ): Dialog {
        return showAlertDialog(ctx, messageId, okPair, null, cancelable)
    }

    fun showAlertDialog(
        ctx: Context, message: CharSequence,
        okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
        cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>? = null,
        cancelable: Boolean = true
    ): Dialog

    fun showAlertDialog(
        ctx: Context, @StringRes messageId: Int,
        okPair: Pair<Int, DialogInterface.OnClickListener?>,
        cancelPair: Pair<Int, DialogInterface.OnClickListener?>? = null,
        cancelable: Boolean = true
    ): Dialog

}

interface Dialog {
    fun dismiss()
}

interface ProgressDialog : Dialog {
    fun setMessage(msg: String)

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?)

}
