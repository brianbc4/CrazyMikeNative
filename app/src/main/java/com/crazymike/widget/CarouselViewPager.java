package com.crazymike.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class CarouselViewPager extends ViewPager {

    Handler handler;
    int currentPosition;
    final int DELAY = 5000;

    public CarouselViewPager(Context context) {
        this(context, null);
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setCurrentItem(currentPosition);
        handler.postDelayed(runnable, DELAY);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (getAdapter() == null||getAdapter().getCount()==0||getAdapter().getCount()==1) return;
            setCurrentItem(++currentPosition >= getAdapter().getCount() ? currentPosition = 0 : currentPosition);
            handler.postDelayed(runnable, DELAY);
        }
    };
}
