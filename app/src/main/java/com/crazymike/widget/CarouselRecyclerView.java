package com.crazymike.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by ChaoJen on 2017/1/12.
 */

public class CarouselRecyclerView extends RecyclerView {

    private static final int DELAY = 5000;
    private static final int PASS_ITEM_COUNT = 2;

    private Handler handler;
    private int maxPosition;
    private int currentPosition;
    private Runnable runnable;

    public CarouselRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int first = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                currentPosition = first;
            }
        });

        runnable = () -> {
            if (currentPosition >= maxPosition - 1 - PASS_ITEM_COUNT)
                scrollToPosition(maxPosition / 2);
            smoothScrollToPosition(currentPosition + PASS_ITEM_COUNT);
            handler.postDelayed(this.runnable, DELAY);
        };
    }

    public void setMaxPosition(int maxPosition) {
        this.maxPosition = maxPosition;
        this.currentPosition = maxPosition / 2;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        scrollToPosition(currentPosition);
        handler.postDelayed(this.runnable, DELAY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }
}
