package com.crazymike.listener;

import android.support.v4.view.ViewPager;

import rx.functions.Action1;

public class OnPageChangedListener implements ViewPager.OnPageChangeListener{

    private Action1<Integer> onPageScrolled;

    public OnPageChangedListener(Action1<Integer> onPageScrolled) {
        this.onPageScrolled = onPageScrolled;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        onPageScrolled.call(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
