package com.crazymike.product.list;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ChaoJen on 2017/1/9.
 */

public class MLinkBannerLayoutManager extends LinearLayoutManager {

    public MLinkBannerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
        public TopSnappedSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return MLinkBannerLayoutManager.this.computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;
        }
    }
}