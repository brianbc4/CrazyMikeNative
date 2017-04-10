package com.crazymike.product.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.Banner;
import com.crazymike.models.RotateJson;

class MLinkBannerAdapter extends RecyclerView.Adapter<MLinkBannerAdapter.ItemHolder> {

    private Context context;
    private Banner banner;
    private OnBannerClickListener listener;

    interface OnBannerClickListener {
        void onBannerClick(Banner banner, RotateJson rotateJson);
    }

    MLinkBannerAdapter(Context context, OnBannerClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ItemHolder(inflater.inflate(R.layout.item_m_link_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        RotateJson rotateJson = banner.getRotate_json().get(position % banner.getRotate_json().size());
        holder.name.setText(rotateJson.getName());
        Glide.with(context).load(rotateJson.getImgurl_mobile()).into(holder.image);
        holder.itemView.setOnClickListener(view -> listener.onBannerClick(banner, rotateJson));
    }

    @Override
    public int getItemCount() {
        return banner.getRotate_json().size() * 100;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;

        ItemHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
