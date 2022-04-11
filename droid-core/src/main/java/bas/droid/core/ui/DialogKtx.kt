package bas.droid.core.ui

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Created by Lucio on 2020-11-02.
 */

fun Context.showLoading(message: CharSequence): ProgressDialog {
    return dialogUI.showLoading(this, message)
}

fun Context.showAlertDialog(@StringRes messageId: Int, @StringRes positiveTextId: Int): Dialog {
    return dialogUI.showAlertDialog(this, messageId, positiveTextId)
}

fun Context.showAlertDialog(message: CharSequence, positiveBtnText: CharSequence): Dialog {
    return dialogUI.showAlertDialog(this, message, positiveBtnText)
}

fun Context.showAlertDialog(
    message: CharSequence,
    okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
    cancelable: Boolean = true
): Dialog {
    return dialogUI.showAlertDialog(this, message, okPair, cancelable)
}

fun Context.showAlertDialog(
    @StringRes messageId: Int,
    okPair: Pair<Int, DialogInterface.OnClickListener?>,
    cancelable: Boolean = true
): Dialog {
    return dialogUI.showAlertDialog(this, messageId, okPair, cancelable)
}

fun Context.showAlertDialog(
    message: CharSequence,
    okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
    cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>? = null,
    cancelable: Boolean = true
): Dialog {
    return dialogUI.showAlertDialog(this, message, okPair, cancelPair, cancelable)
}

fun Context.showAlertDialog(
    @StringRes messageId: Int,
    okPair: Pair<Int, DialogInterface.OnClickListener?>,
    cancelPair: Pair<Int, DialogInterface.OnClickListener?>? = null,
    cancelable: Boolean = true
): Dialog {
    return dialogUI.showAlertDialog(this, messageId, okPair, cancelPair, cancelable)
}


fun Fragment.showLoading(message: CharSequence): ProgressDialog {
    return requireContext().showLoading(message)
}

fun Fragment.showAlertDialog(@StringRes messageId: Int, @StringRes positiveTextId: Int): Dialog {
    return requireContext().showAlertDialog(messageId, positiveTextId)
}

fun Fragment.showAlertDialog(message: CharSequence, positiveBtnText: CharSequence): Dialog {
    return requireContext().showAlertDialog(message, positiveBtnText)
}

fun Fragment.showAlertDialog(
    message: CharSequence,
    okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
    cancelable: Boolean = true
): Dialog {
    return requireContext().showAlertDialog(message, okPair, cancelable)
}

fun Fragment.showAlertDialog(
    @StringRes messageId: Int,
    okPair: Pair<Int, DialogInterface.OnClickListener?>,
    cancelable: Boolean = true
): Dialog {
    return requireContext().showAlertDialog(messageId, okPair, cancelable)
}

fun Fragment.showAlertDialog(
    message: CharSequence,
    okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
    cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>? = null,
    cancelable: Boolean = true
): Dialog {
    return requireContext().showAlertDialog(message, okPair, cancelPair, cancelable)
}

fun Fragment.showAlertDialog(
    @StringRes messageId: Int,
    okPair: Pair<Int, DialogInterface.OnClickListener?>,
    cancelPair: Pair<Int, DialogInterface.OnClickListener?>? = null,
    cancelable: Boolean = true
): Dialog {
    return requireContext().showAlertDialog(messageId, okPair, cancelPair, cancelable)
}


fun View.showLoading(message: CharSequence): ProgressDialog {
    return context.showLoading(message)
}

fun View.showAlertDialog(@StringRes messageId: Int, @StringRes positiveTextId: Int): Dialog {
    return context.showAlertDialog(messageId, positiveTextId)
}

fun View.showAlertDialog(message: CharSequence, positiveBtnText: CharSequence): Dialog {
    return context.showAlertDialog(message, positiveBtnText)
}

fun View.showAlertDialog(
    message: CharSequence,
    okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
    cancelable: Boolean = true
): Dialog {
    return context.showAlertDialog(message, okPair, cancelable)
}

fun View.showAlertDialog(
    @StringRes messageId: Int,
    okPair: Pair<Int, DialogInterface.OnClickListener?>,
    cancelable: Boolean = true
): Dialog {
    return context.showAlertDialog(messageId, okPair, cancelable)
}

fun View.showAlertDialog(
    message: CharSequence,
    okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
    cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>? = null,
    cancelable: Boolean = true
): Dialog {
    return context.showAlertDialog(message, okPair, cancelPair, cancelable)
}

fun View.showAlertDialog(
    @StringRes messageId: Int,
    okPair: Pair<Int, DialogInterface.OnClickListener?>,
    cancelPair: Pair<Int, DialogInterface.OnClickListener?>? = null,
    cancelable: Boolean = true
): Dialog {
    return context.showAlertDialog(messageId, okPair, cancelPair, cancelable)
}