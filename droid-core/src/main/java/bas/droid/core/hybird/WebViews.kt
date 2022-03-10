/**
 * Created by Lucio on 2022/1/26.
 */


package bas.droid.core.hybird

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.webkit.WebView



/**
 * 设置透明背景
 */
fun WebView.applyTransparentBackground(){
    background = ColorDrawable(Color.TRANSPARENT).also {
        it.alpha = 0
    }
//    setBackgroundColor(Color.TRANSPARENT)
//    background?.alpha = 0
}