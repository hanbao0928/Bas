package bas.android.view.label;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.appcompat.widget.AppCompatImageView;

public class LabelImageView extends AppCompatImageView {

    LabelViewHelper labelHelper;

    public LabelImageView(Context context) {
        this(context, null);
    }

    public LabelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        labelHelper = new LabelViewHelper(this, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        labelHelper.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        labelHelper.onDraw(canvas);
    }

    public void setLabelText(@Nullable String text) {
        labelHelper.setText(text);
    }

    public void setLabelTextSize(@Px int textSizePx) {
        labelHelper.setTextSize(textSizePx);
    }

    public void setLabelTextColor(@ColorInt int textColor) {
        labelHelper.setTextColor(textColor);
    }

    public void setTextStyle(@LabelViewHelper.TextStyle int textStyle) {
        labelHelper.setTextStyle(textStyle);
    }

    public void setLabelLocation(@LabelViewHelper.Location int location) {
        labelHelper.setLocation(location);
    }

    public void setLabelDistance(@Px int distancePx) {
        labelHelper.setDistance(distancePx);
    }

    public void setLabelBackgroundColor(@ColorInt int backgroundColor) {
        labelHelper.setBackgroundColor(backgroundColor);
    }

    public void setLabelPadding(@Px int paddingPx) {
        labelHelper.setPadding(paddingPx);
    }

    public void setLabelStrokeWidth(@Px int strokeWidthPx) {
        labelHelper.setStrokeWidth(strokeWidthPx);
    }

    public void setLabelStrokeColor(@ColorInt int strokeColor) {
        labelHelper.setStrokeColor(strokeColor);
    }

    public void setLabelYAxisDegree(@IntRange(from = 0, to = 90) int degree) {
        labelHelper.setYAxisDegree(degree);
    }

    public void setLabelVisible(boolean isVisible){
        labelHelper.setVisible(isVisible);
    }
}


