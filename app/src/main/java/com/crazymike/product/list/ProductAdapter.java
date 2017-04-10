package com.crazymike.product.list;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.Banner;
import com.crazymike.models.ItemList;
import com.crazymike.models.RotateJson;
import com.crazymike.util.Util;
import com.crazymike.widget.CarouselRecyclerView;
import com.crazymike.widget.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BannerAdapter.OnBannerClickListener {

    private static final String TAG = ProductAdapter.class.getSimpleName();

    private static final int TYPE_BANNER_LINK = 0;
    private static final int TYPE_BANNER_M_LINK = 1;
    private static final int TYPE_BANNER_M_MULTI_LINK = 2;
    private static final int TYPE_BANNER_ROTATE = 3;
    private static final int TYPE_ITEM_LIST = 4;

    private static final int BANNER_WIDTH_1 = 900;
    private static final int BANNER_HEIGHT_1 = 300;
    private static final int BANNER_WIDTH_2 = 500;
    private static final int BANNER_HEIGHT_2 = 249;

    private Activity activity;
    private GridLayoutManager gridLayoutManager;
    private String tag;
    private List<Banner> banners;
    private List<ItemList> itemLists;
    private List<Integer> trackList;
    private boolean isTravelTag = false;
    private OnItemClickListener itemClickListener;

    interface OnItemClickListener {

        void onClick(ItemList itemList);

        void onCartClick(ItemList itemList);

        void onLikeClick(ItemList itemList);

        void onBannerClick(Banner banner, RotateJson rotateJson);
    }

    ProductAdapter(Activity activity, GridLayoutManager gridLayoutManager, List<Banner> banners, List<ItemList> itemLists, String tag, List<Integer> trackList, OnItemClickListener itemClickListener) {
        this.activity = activity;
        this.tag = tag;
        this.banners = banners;
        this.itemLists = itemLists;
        this.trackList = trackList;
        this.itemClickListener = itemClickListener;
        this.gridLayoutManager = gridLayoutManager;
    }

    private void checkNullData() {
        if (banners == null) banners = new ArrayList<>();
        if (itemLists == null) itemLists = new ArrayList<>();
        if (trackList == null) trackList = new ArrayList<>();
    }

    private void setupSpanSize() {
        if (gridLayoutManager == null) return;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (banners != null && position < banners.size()) {
                    if (getItemViewType(position) == TYPE_BANNER_LINK)
                        return gridLayoutManager.getSpanCount() / 2;
                    return gridLayoutManager.getSpanCount();
                } else return 2;
            }
        });
    }

    ProductAdapter setBanners(List<Banner> banners) {
        this.banners = banners;
        notifyDataSetChanged();
        return this;
    }

    ProductAdapter setItemList(List<ItemList> itemLists) {
        this.itemLists = itemLists;
        notifyDataSetChanged();
        return this;
    }

    ProductAdapter setTrackList(List<Integer> trackList) {
        this.trackList = trackList;
        notifyDataSetChanged();
        return this;
    }

    ProductAdapter setIsTravelTag(boolean isTravelTag) {
        this.isTravelTag = isTravelTag;
        return this;
    }

    @Override
    public int getItemCount() {
        checkNullData();
        setupSpanSize();
        return banners.size() + itemLists.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position < banners.size()) {
            //type define do not match doc
            switch (banners.get(position).getType()) {
                case "LINK":

                    //return TYPE_BANNER_LINK;
                    return TYPE_BANNER_M_MULTI_LINK;

                case "MLINK":
                    return TYPE_BANNER_M_LINK;
                case "MLINK2":
                    return TYPE_BANNER_LINK;

                case "MLINK3":
                case "MLINK4":
                    //return TYPE_BANNER_M_MULTI_LINK;
                    return TYPE_BANNER_LINK;

                case "ROTATE1":
                case "ROTATE2":
                case "ROTATE3":
                    return TYPE_BANNER_ROTATE;
            }
        }

        return TYPE_ITEM_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case TYPE_BANNER_LINK:
                return new LinkBannerHolder(inflater.inflate(R.layout.holder_link_banner, parent, false));
            case TYPE_BANNER_M_LINK:
                return new MLinkBannerHolder(inflater.inflate(R.layout.holder_m_link_banner, parent, false));
            case TYPE_BANNER_M_MULTI_LINK:
                return null;
            case TYPE_BANNER_ROTATE:
                return new RotateBannerHolder(inflater.inflate(R.layout.holder_rotate_banner, parent, false));
            default:
                return new ItemListHolder(inflater.inflate(R.layout.holder_item_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LinkBannerHolder) {
            RotateJson rotateJson = banners.get(position).getRotate_json().get(0);
            Glide.with(activity).load(rotateJson.getImgurl_mobile()).into(((LinkBannerHolder) holder).image);
            holder.itemView.setOnClickListener(view -> itemClickListener.onBannerClick(banners.get(position), rotateJson));
        }

        if (holder instanceof MLinkBannerHolder) {
            MLinkBannerHolder mLinkBannerHolder =  ((MLinkBannerHolder) holder);

            mLinkBannerHolder.mLinkBannerAdapter.setBanner(banners.get(position));
            mLinkBannerHolder.recyclerView.setMaxPosition(mLinkBannerHolder.mLinkBannerAdapter.getItemCount());
        }

        if (holder instanceof RotateBannerHolder) {
            Banner banner = banners.get(position);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((RotateBannerHolder) holder).viewPager.getLayoutParams();
            params.height = getBannerHeight(tag);
            ((RotateBannerHolder) holder).viewPager.setLayoutParams(params);

            BannerAdapter adapter = new BannerAdapter(activity, banner, this);
            ((RotateBannerHolder) holder).viewPager.setAdapter(adapter);
            ((RotateBannerHolder) holder).viewPager.setCurrentItem(adapter.getCount() / 2);
            ((RotateBannerHolder) holder).indicator.setMaxCount(banner.getRotate_json().size());//need to setup before setViewPager
            ((RotateBannerHolder) holder).indicator.setViewPager(((RotateBannerHolder) holder).viewPager);
            ((RotateBannerHolder) holder).indicator.setVisibility(adapter.getCount() == 1 ? View.GONE : View.VISIBLE);
        }

        if (holder instanceof ItemListHolder) {
            ItemList itemList = itemLists.get(position - banners.size());
            String imgUrl = itemList.getPimg_m() == null || itemList.getPimg_m().equals("") ? itemList.getPimg_m() : itemList.getPimg();
            Glide.with(activity).load(imgUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.bg_download)
                    .into(((ItemListHolder) holder).thumb);

            ((ItemListHolder) holder).stock.setVisibility(itemList.getStock().equals("0") || itemList.getStock().equals("") ? View.VISIBLE : View.GONE);
            ((ItemListHolder) holder).buyItem.setText(itemList.getBuy_items());

            String disc = Util.getDiscount(itemList.getPrice_fake(), itemList.getPrice_title());
            ((ItemListHolder) holder).discount.setText(String.format("%s%s", disc, activity.getString(R.string.disc)));
            ((ItemListHolder) holder).discount.setVisibility(disc.equals("") ? View.GONE : View.VISIBLE);

            ((ItemListHolder) holder).priceFake.setText(String.format("$%s", itemList.getPrice_fake()));
            Paint paint = ((ItemListHolder) holder).priceFake.getPaint();
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);

            if (itemList.getPrice_title() != null && !itemList.getPrice_title().equals("0") && itemList.getIs_priceup().equals("1")) {
                ((ItemListHolder) holder).priceUp.setVisibility(View.VISIBLE);
                ((ItemListHolder) holder).priceTitle.setText(String.format("$%s", itemList.getPrice_title()));
            } else {
                ((ItemListHolder) holder).priceUp.setVisibility(View.GONE);
                ((ItemListHolder) holder).priceTitle.setText(String.format("$%s", itemList.getPrice()));
            }

            ((ItemListHolder) holder).name.setText(itemList.getPname());

            boolean isNew = itemList.getIs_new() != null && itemList.getIs_new().equals("t");
            boolean is72hours = itemList.getIs_72hours() != null && itemList.getIs_72hours().equals("t");
            boolean isConvenienceStore = itemList.getIs_711() != null && (itemList.getIs_711().equals("t") || itemList.getIs_ok().equals("t") || itemList.getIs_fm().equals("t") || itemList.getIs_hl().equals("t"));

            ((ItemListHolder) holder).show_deliver.setBackground((!isNew && !is72hours && !isConvenienceStore) ? null : activity.getResources().getDrawable(R.drawable.background_divider_right));

            if(itemList.getSwrule_deliver_hours().equals("24")){
                Glide.with(activity).load(R.mipmap.deliver_24hr).into(((ItemListHolder) holder).deliver_icon);
                ((ItemListHolder) holder).show_deliver.setVisibility(View.VISIBLE);
            }else if(itemList.getSwrule_deliver_hours().equals("48")){
                Glide.with(activity).load(R.mipmap.deliver_48hr).into(((ItemListHolder) holder).deliver_icon);
                ((ItemListHolder) holder).show_deliver.setVisibility(View.VISIBLE);
            }else{
                ((ItemListHolder) holder).show_deliver.setVisibility(View.GONE);
            }

            ((ItemListHolder) holder).isNew.setVisibility(isNew ? View.VISIBLE : View.GONE);
            ((ItemListHolder) holder).is72hours.setVisibility(is72hours ? View.VISIBLE : View.GONE);
            ((ItemListHolder) holder).isConvenienceStore.setVisibility(isConvenienceStore ? View.VISIBLE : View.GONE);

            ((ItemListHolder) holder).track.setImageResource(isTrack(itemList.getItem_id()) ? R.mipmap.like_active : R.mipmap.like40x40);

            holder.itemView.setOnClickListener(view -> itemClickListener.onClick(itemList));
            ((ItemListHolder) holder).cart.setOnClickListener(view -> itemClickListener.onCartClick(itemList));
            ((ItemListHolder) holder).track.setOnClickListener(view -> itemClickListener.onLikeClick(itemList));

            //if item is travel/hi-life , can not add to cart
            if (itemList.getIs_hl() != null && itemList.getIs_hl().equals("t") || isTravelTag) {//4432 => travel tag
                ((ItemListHolder) holder).cart.setVisibility(View.GONE);
            }

            if (itemList.getStock().equals("0") || itemList.getStock().equals(""))
                ((ItemListHolder) holder).cart.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBannerClick(Banner banner, RotateJson rotateJson) {
        itemClickListener.onBannerClick(banner, rotateJson);
    }

    private int getBannerHeight(String channel) {
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return channel.equals("5") || isTravelTag ? screenWidth * BANNER_HEIGHT_1 / BANNER_WIDTH_1 : screenWidth * BANNER_HEIGHT_2 / BANNER_WIDTH_2;
    }

    private boolean isTrack(String itemId) {
        for (Integer trackId : trackList) {
            if (String.valueOf(trackId).equals(itemId)) return true;
        }
        return false;
    }

    private class LinkBannerHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        LinkBannerHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    private class MLinkBannerHolder extends RecyclerView.ViewHolder {

        CarouselRecyclerView recyclerView;
        MLinkBannerAdapter mLinkBannerAdapter;

        MLinkBannerHolder(View itemView) {
            super(itemView);
            recyclerView = (CarouselRecyclerView) itemView.findViewById(R.id.recyclerView);

            recyclerView.setLayoutManager(new MLinkBannerLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(mLinkBannerAdapter = new MLinkBannerAdapter(activity, (banner, rotateJson) -> itemClickListener.onBannerClick(banner, rotateJson)));
        }
    }

//    private class MLinkMultiBannerHolder extends RecyclerView.ViewHolder {
//
//        public MLinkMultiBannerHolder(View itemView) {
//            super(itemView);
//        }
//    }

    private class RotateBannerHolder extends RecyclerView.ViewHolder {

        ViewPager viewPager;
        CircleIndicator indicator;

        RotateBannerHolder(View itemView) {
            super(itemView);
            viewPager = (ViewPager) itemView.findViewById(R.id.viewPager);
            indicator = (CircleIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    private class ItemListHolder extends RecyclerView.ViewHolder {

        ImageView thumb;
        ImageView stock;
        TextView name;
        TextView priceTitle;
        TextView priceFake;
        TextView priceUp;
        TextView buyItem;
        TextView discount;
        View isNew;
        View is72hours;
        View isConvenienceStore;
        RelativeLayout show_deliver;
        ImageView deliver_icon;
        ImageView cart;
        ImageView track;

        ItemListHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            stock = (ImageView) itemView.findViewById(R.id.stock);
            name = (TextView) itemView.findViewById(R.id.name);
            priceTitle = (TextView) itemView.findViewById(R.id.priceTitle);
            priceFake = (TextView) itemView.findViewById(R.id.priceFake);
            priceUp = (TextView) itemView.findViewById(R.id.priceUp);
            buyItem = (TextView) itemView.findViewById(R.id.text_view_buy_items);
            discount = (TextView) itemView.findViewById(R.id.discount);
            isNew = itemView.findViewById(R.id.isNew);
            show_deliver = (RelativeLayout) itemView.findViewById(R.id.show_deliver);
            deliver_icon = (ImageView) itemView.findViewById(R.id.deliver_icon);
            is72hours = itemView.findViewById(R.id.is72hours);
            isConvenienceStore = itemView.findViewById(R.id.isConvenienceStore);
            cart = (ImageView) itemView.findViewById(R.id.cart);
            track = (ImageView) itemView.findViewById(R.id.image_view_favorite);
        }
    }

}
