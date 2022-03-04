package bas.droid.view.label;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.imageview.ShapeableImageView;

import bas.droid.view.label.annotation.LabelView;

/**
 * Created by Lucio on 2021/12/12.
 * 生成对应支持
 */
@LabelView({View.class,AppCompatImageView.class, AppCompatTextView.class, AppCompatButton.class, ShapeableImageView.class})
class LabelViewInject {
}
