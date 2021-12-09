package bas.leanback.layout;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import bas.leanback.layout.annotation.EffectLayout;

/**
 * Created by Lucio on 2021/12/9.
 * 注入EffectLayout，不支持LinearLayout
 */
@EffectLayout({LinearLayout.class,RelativeLayout.class, FrameLayout.class, ConstraintLayout.class})
class EffectLayoutInject {
}
