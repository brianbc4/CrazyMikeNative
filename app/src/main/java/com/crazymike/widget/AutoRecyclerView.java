package com.crazymike.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;

import com.crazymike.R;

public class AutoRecyclerView extends RecyclerView {

    private int defaultCellWidth;
    private int spanCountOffset;
    private boolean even, odd;

    public AutoRecyclerView(Context context) {
        this(context, null);
    }

    public AutoRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.AutoRecyclerView, 0, 0);
        defaultCellWidth = (int) typedArray.getDimension(R.styleable.AutoRecyclerView_default_item_width, 0);
        spanCountOffset = typedArray.getInteger(R.styleable.AutoRecyclerView_spanCountOffset, 0);
        odd = typedArray.getBoolean(R.styleable.AutoRecyclerView_odd, false);
        even = typedArray.getBoolean(R.styleable.AutoRecyclerView_even, false);
        typedArray.recycle();

    }

    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {

        if (getAdapter() != null && getLayoutManager() instanceof GridLayoutManager) {

            GridLayoutAnimationController.AnimationParameters animationParams = (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;

            if (animationParams == null) {
                animationParams = new GridLayoutAnimationController.AnimationParameters();
                params.layoutAnimationParameters = animationParams;
            }

            int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();

            animationParams.count = count;
            animationParams.index = index;
            animationParams.columnsCount = columns;
            animationParams.rowsCount = count / columns;

            final int invertedIndex = count - 1 - index;
            animationParams.column = columns - 1 - (invertedIndex % columns);
            animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns;

        } else {
            super.attachLayoutAnimationParameters(child, params, index, count);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof GridLayoutManager)) return;

        int lastWidth = -1;

        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        if (width == lastWidth || width <= 0) {
            return;
        }

        // Compute number of columns
        int numColumns = 1;
        while (true) {
            if (width / numColumns > defaultCellWidth) {
                numColumns += odd || even ? 2 : 1;
            } else {
                break;
            }
        }

        if (odd) {
            numColumns = numColumns % 2 == 0 ? numColumns + 1 : numColumns;
        }

        if (even) {
            numColumns = numColumns % 2 == 0 ? numColumns : numColumns + 1;
        }

        if (numColumns != ((GridLayoutManager) layoutManager).getSpanCount()) {
            ((GridLayoutManager) layoutManager).setSpanCount(numColumns);
        }
    }
}