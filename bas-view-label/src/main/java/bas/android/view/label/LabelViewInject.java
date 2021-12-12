package bas.android.view.label;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.imageview.ShapeableImageView;

import bas.leanback.layout.annotation.LabelView;

/**
 * Created by Lucio on 2021/12/12.
 */
@LabelView({View.class,AppCompatImageView.class, AppCompatTextView.class, AppCompatButton.class, ShapeableImageView.class})
class LabelViewInject {
}
