package com.crazymike.search.result;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.ItemList;
import com.crazymike.util.Util;

import java.util.List;

/**
 * Created by cuber on 2016/11/9.
 */

class SearchQueryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Listener listener;
    private List<ItemList> itemLists;
    private List<Integer> trackList;
    private boolean isTravelTag;

    interface Listener {

        void onClick(ItemList itemList);

        void onCartClick(ItemList itemList);

        void onLikeClick(ItemList itemList);
    }

    SearchQueryAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    SearchQueryAdapter setItemList(List<ItemList> itemLists) {
        this.itemLists = itemLists;
        notifyDataSetChanged();
        return this;
    }

    SearchQueryAdapter setTrackList(List<Integer> trackList) {
        this.trackList = trackList;
        notifyDataSetChanged();
        return this;
    }

    public SearchQueryAdapter isTravelTag(boolean travelTag) {
        isTravelTag = travelTag;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new SearchQueryAdapter.ItemListHolder(inflater.inflate(R.layout.holder_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SearchQueryAdapter.ItemListHolder) {
            ItemList itemList = itemLists.get(position);
            String imgUrl = itemList.getPimg_m() == null || itemList.getPimg_m().equals("") ? itemList.getPimg_m() : itemList.getPimg();
            Glide.with(context).load(imgUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.bg_download)
                    .into(((SearchQueryAdapter.ItemListHolder) holder).thumb);

            ((SearchQueryAdapter.ItemListHolder) holder).stock.setVisibility(itemList.getStock().equals("0") || itemList.getStock().equals("") ? View.VISIBLE : View.GONE);
            ((SearchQueryAdapter.ItemListHolder) holder).buyItem.setText(itemList.getBuy_items());

            String disc = Util.getDiscount(itemList.getPrice_fake(), itemList.getPrice_title());
            ((SearchQueryAdapter.ItemListHolder) holder).discount.setText(String.format("%s%s", disc, context.getString(R.string.disc)));
            ((SearchQueryAdapter.ItemListHolder) holder).discount.setVisibility(disc.equals("") ? View.GONE : View.VISIBLE);

            ((SearchQueryAdapter.ItemListHolder) holder).priceFake.setText(String.format("$%s", itemList.getPrice_fake()));
            Paint paint = ((SearchQueryAdapter.ItemListHolder) holder).priceFake.getPaint();
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);

            if (itemList.getPrice_title() != null && !itemList.getPrice_title().equals("0") && itemList.getIs_priceup().equals("1")) {
                ((SearchQueryAdapter.ItemListHolder) holder).priceUp.setVisibility(View.VISIBLE);
                ((SearchQueryAdapter.ItemListHolder) holder).priceTitle.setText(String.format("$%s", itemList.getPrice_title()));
            } else {
                ((SearchQueryAdapter.ItemListHolder) holder).priceUp.setVisibility(View.GONE);
                ((SearchQueryAdapter.ItemListHolder) holder).priceTitle.setText(String.format("$%s", itemList.getPrice()));
            }

            ((SearchQueryAdapter.ItemListHolder) holder).name.setText(itemList.getPname());

            ((SearchQueryAdapter.ItemListHolder) holder).isNew.setVisibility(itemList.getIs_new() != null && itemList.getIs_new().equals("t") ? View.VISIBLE : View.GONE);
            ((SearchQueryAdapter.ItemListHolder) holder).is72hours.setVisibility(itemList.getIs_72hours() != null && itemList.getIs_72hours().equals("t") ? View.VISIBLE : View.GONE);
            ((SearchQueryAdapter.ItemListHolder) holder).isConvenienceStore.setVisibility(itemList.getIs_711() != null &&
                    (itemList.getIs_711().equals("t") || itemList.getIs_ok().equals("t") || itemList.getIs_fm().equals("t") || itemList.getIs_hl().equals("t")) ? View.VISIBLE : View.GONE);

            ((SearchQueryAdapter.ItemListHolder) holder).track.setImageResource(isTrack(itemList.getItem_id()) ? R.mipmap.like_active : R.mipmap.like40x40);

            holder.itemView.setOnClickListener(view -> listener.onClick(itemList));
            ((SearchQueryAdapter.ItemListHolder) holder).cart.setOnClickListener(view -> listener.onCartClick(itemList));
            ((SearchQueryAdapter.ItemListHolder) holder).track.setOnClickListener(view -> listener.onLikeClick(itemList));

            ((SearchQueryAdapter.ItemListHolder) holder).cart.setVisibility(isTravelTag || (itemList.getIs_hilife_shop() != null && itemList.getIs_hilife_shop().equals("t")) ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemLists == null ? 0 : itemLists.size();
    }

    private boolean isTrack(String itemId) {
        for (Integer trackId : trackList) {
            if (String.valueOf(trackId).equals(itemId)) return true;
        }
        return false;
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
            is72hours = itemView.findViewById(R.id.is72hours);
            isConvenienceStore = itemView.findViewById(R.id.isConvenienceStore);
            cart = (ImageView) itemView.findViewById(R.id.cart);
            track = (ImageView) itemView.findViewById(R.id.image_view_favorite);
        }
    }
}
