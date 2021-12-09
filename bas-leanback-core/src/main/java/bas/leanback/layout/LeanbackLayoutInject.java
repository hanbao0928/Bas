package bas.leanback.layout;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import bas.leanback.layout.annotation.LeanbackLayout;

/**
 * Created by Lucio on 2021/11/30.
 */
@LeanbackLayout({LinearLayout.class, RelativeLayout.class, FrameLayout.class, ConstraintLayout.class})
class LeanbackLayoutInject {
}
