package com.crazymike.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class OnScrollTopAndBottomListener extends RecyclerView.OnScrollListener {

    private String TAG = getClass().getSimpleName();
    private int firstVisibleItemPosition;
    private int lastVisibleItemPosition;

    private OnUpScrollListener onUpScrollListener;
    private OnDownScrollListener onDownScrollListener;
    private OnBottomListener onBottomListener;
    private OnTopListener onTopListener;

    private int upGap=1, downGap=1;
    private boolean isScrollDown;
    private boolean isBottom;
    private boolean isTop;
    private int itemCount = 0;


    public interface OnUpScrollListener {
        void onUpScroll();
    }

    public interface OnDownScrollListener {
        void onDownScroll();
    }

    public interface OnBottomListener {
        void onBottom();
    }

    public interface OnTopListener {
        void onTop();
    }

    public OnScrollTopAndBottomListener setOnUpScrollListener(int upGap, OnUpScrollListener onUpScrollListener) {
        this.upGap = upGap;
        this.onUpScrollListener = onUpScrollListener;
        return this;
    }

    public OnScrollTopAndBottomListener setOnDownScrollListener(int downGap, OnDownScrollListener onDownScrollListener) {
        this.downGap = downGap;
        this.onDownScrollListener = onDownScrollListener;
        return this;
    }

    public OnScrollTopAndBottomListener setOnBottomListener(OnBottomListener onBottomListener) {
        this.onBottomListener = onBottomListener;
        return this;
    }

    public OnScrollTopAndBottomListener setOnBottomListener(int itemCount, OnBottomListener onBottomListener) {
        this.itemCount = itemCount;
        this.onBottomListener = onBottomListener;
        return this;
    }

    public OnScrollTopAndBottomListener setOnTopListener(OnTopListener onTopListener) {
        this.onTopListener = onTopListener;
        return this;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        isBottom = false;
        isTop = false;

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        }

        if (dy > 0) {
            if (isScrollDown && lastVisibleItemPosition % upGap == 0) {
                isScrollDown = false;
                if (onUpScrollListener != null) {
                    onUpScrollListener.onUpScroll();
                }
            }
        } else {
            if (!isScrollDown && firstVisibleItemPosition % upGap == 0) {
                isScrollDown = true;
                if (onDownScrollListener != null) {
                    onDownScrollListener.onDownScroll();
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - itemCount - 1 && !isBottom) {
            isBottom = true;
            if (onBottomListener != null) onBottomListener.onBottom();
            Log.i("onBottom", "onBottom");
        }

        if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && firstVisibleItemPosition == 0 && !isTop) {
            isTop = true;
            if (onTopListener != null) onTopListener.onTop();
            Log.i("onTop", "onTop");
        }
    }
}
