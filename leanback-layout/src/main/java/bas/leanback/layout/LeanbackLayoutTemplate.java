package bas.leanback.layout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * LeanbackLayout AnnotationProcessor 的模板代码
 */
public class LeanbackLayoutTemplate extends LinearLayout implements LeanbackLayoutHelper.Callback {

    private final LeanbackLayoutHelper layoutHelper;


    public LeanbackLayoutTemplate(@NonNull Context context) {
        this(context, null);
    }

    public LeanbackLayoutTemplate(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeanbackLayoutTemplate(@NonNull Context context, @Nullable AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            layoutHelper = null;
        } else {
            boolean isSupportBringChildToFront = BringToFrontHelper.isLayoutSupportBringChildToFront(this);
            this.setChildrenDrawingOrderEnabled(!isSupportBringChildToFront);
            layoutHelper = LeanbackLayoutHelper.create(this, this, attrs, defStyleAttr);
        }
    }

    @Override
    public void callSuperBringChildToFront(@Nullable View child) {
        super.bringChildToFront(child);
    }

    @Override
    public int callSuperGetChildDrawingOrder(int childCount, int drawingPosition) {
        return super.getChildDrawingOrder(childCount, drawingPosition);
    }

    @Override
    public boolean isChildrenDrawingOrderEnabled() {
        return super.isChildrenDrawingOrderEnabled();
    }

    @Override
    public boolean dispatchUnhandledMove(@Nullable View focused, int direction) {
        if (super.dispatchUnhandledMove(focused, direction)) {
            return true;
        } else {
            return layoutHelper.dispatchUnhandledMove(focused, direction);
        }
    }

    @Override
    public void bringChildToFront(@Nullable View child) {
        layoutHelper.bringChildToFront(child);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int drawingPosition) {
        return layoutHelper.getChildDrawingOrder(childCount, drawingPosition);
    }

    @Override
    public void requestChildFocus(@Nullable View child, @Nullable View focused) {
        super.requestChildFocus(child, focused);
        layoutHelper.requestChildFocus(child, focused);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (!layoutHelper.addFocusables(views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction,
                                                  @Nullable Rect previouslyFocusedRect) {
        if (!layoutHelper.onRequestFocusInDescendants(direction, previouslyFocusedRect)) {
            return true;
        } else {
            return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onViewRemoved(@Nullable View child) {
        super.onViewRemoved(child);
        layoutHelper.onViewRemoved(child);
    }
}
