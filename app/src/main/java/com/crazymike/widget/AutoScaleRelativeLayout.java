package com.crazymike.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.crazymike.R;

public class AutoScaleRelativeLayout extends RelativeLayout {

    int scaleX, scaleY;
    boolean byHeight;

    public AutoScaleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoScaleRelativeLayout);
        scaleX = a.getInteger(R.styleable.AutoScaleRelativeLayout_scale_width, 1);
        scaleY = a.getInteger(R.styleable.AutoScaleRelativeLayout_scale_height, 1);
        byHeight = a.getBoolean(R.styleable.AutoScaleRelativeLayout_by_height, false);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int measureSpecWidth, measureSpecHeight;
        if (byHeight) {
            measureSpecWidth = MeasureSpec.makeMeasureSpec(height * scaleX / scaleY, MeasureSpec.EXACTLY);
            measureSpecHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        } else {
            measureSpecWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            measureSpecHeight = MeasureSpec.makeMeasureSpec(width * scaleY / scaleX, MeasureSpec.EXACTLY);
        }
        super.onMeasure(measureSpecWidth, measureSpecHeight);
    }
}
