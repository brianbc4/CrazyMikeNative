package com.crazymike.product.detail.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.Img;

import java.util.List;

class ImgPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Img> imgs;

    ImgPagerAdapter(Context context, List<Img> imgs) {
        this.context = context;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return imgs.size();
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
        View view = LayoutInflater.from(context).inflate(R.layout.page_img, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.img);

        Glide.with(context)
                .load(imgs.get(position).getUrl())
                .placeholder(R.mipmap.bg_download)
                .centerCrop()
                .into(img);

        container.addView(view);
        return view;
    }
}