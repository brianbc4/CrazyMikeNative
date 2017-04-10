package com.crazymike.search.box;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.models.SearchHotKey;

import java.util.List;

class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnHotKeyClickListener listener;
    private List<SearchHotKey> hotKeyList;

    interface OnHotKeyClickListener {
        void onHotKeyClick(SearchHotKey searchHotKey);
    }

    SearchAdapter(Context context, List<SearchHotKey> hotKeyList, OnHotKeyClickListener listener) {
        this.context = context;
        this.hotKeyList = hotKeyList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new HotKeyViewHolder(inflater.inflate(R.layout.itme_search_hot_key, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HotKeyViewHolder) {
            SearchHotKey hotKey = hotKeyList.get(position);
            ((HotKeyViewHolder) holder).hotKey.setText(hotKey.getHotkey());
        }
    }

    @Override
    public int getItemCount() {
        return hotKeyList == null ? 0 : hotKeyList.size();
    }

    private class HotKeyViewHolder extends RecyclerView.ViewHolder {

        private TextView hotKey;

        HotKeyViewHolder(View itemView) {
            super(itemView);
            hotKey = (TextView) itemView.findViewById(R.id.searchHotKey);
            itemView.setOnClickListener(view -> listener.onHotKeyClick(hotKeyList.get(getAdapterPosition())));
        }
    }
}
