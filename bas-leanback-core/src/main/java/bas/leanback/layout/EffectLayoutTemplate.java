package bas.leanback.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by Lucio on 2021/12/15.
 * EffectLayout模板代码
 */
class EffectLayoutTemplate extends ConstraintLayout implements EffectLayoutDelegate.Callback {
    private final EffectLayoutDelegate effectDelegate;

    public EffectLayoutTemplate(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public EffectLayoutTemplate(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EffectLayoutTemplate(@NonNull Context context, @Nullable AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        effectDelegate = EffectLayoutDelegate.create(this, this, attrs, defStyleAttr);
    }

    public EffectLayoutTemplate(@NonNull Context context, @NonNull EffectParams effectParams) {
        super(context);
        effectDelegate = EffectLayoutDelegate.create(this, this, effectParams);
    }

    @Override
    public void callSuperDispatchDraw(@Nullable Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public void callSuperDraw(@Nullable Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void callSuperOnDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        effectDelegate.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void dispatchDraw(@Nullable Canvas canvas) {
        effectDelegate.dispatchDraw(canvas);
    }

    @SuppressLint(value = "MissingSuperCall")
    @Override
    public void draw(@Nullable Canvas canvas) {
        assert canvas != null;
        effectDelegate.draw(canvas);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction,
                                  @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        effectDelegate.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public void onViewAdded(@Nullable View child) {
        super.onViewAdded(child);
        effectDelegate.onViewAdded(child);
    }

    @SuppressLint(value ="MissingSuperCall")
    @Override
    protected void onDetachedFromWindow() {
        effectDelegate.onDetachedFromWindow();
    }
}
