package com.crazymike.product.list;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.Banner;
import com.crazymike.models.RotateJson;

class BannerAdapter extends PagerAdapter {

    private static final int MAX_COUNT = 100;

    private Activity activity;
    private Banner banner;
    private OnBannerClickListener listener;

    interface OnBannerClickListener {
        void onBannerClick(Banner banner, RotateJson rotateJson);
    }

    BannerAdapter(Activity activity, Banner banner, OnBannerClickListener listener) {
        this.activity = activity;
        this.banner = banner;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return banner.getRotate_json().size() == 0 ? 0 : banner.getRotate_json().size() == 1 ? 1 : MAX_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(activity).inflate(R.layout.page_img, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.img);

        position = position % banner.getRotate_json().size();

        RotateJson rotate = banner.getRotate_json().get(position);
        String imgUrl = rotate.getImgurl_mobile() == null || rotate.getImgurl_mobile().equals("") ? rotate.getImgurl() : rotate.getImgurl_mobile();

        Glide.with(activity)
                .load(imgUrl)
                .centerCrop()
                .into(img);

        container.addView(view);
        view.setOnClickListener(v -> listener.onBannerClick(banner, rotate));
        return view;
    }
}
