package com.crazymike.product.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.models.General;

import java.util.List;

public class GeneralTagAdapter extends RecyclerView.Adapter<GeneralTagAdapter.TagViewHolder> {

    private Context context;
    private List<General.Tag> tagList;

    public GeneralTagAdapter(Context context, List<General.Tag> tagList) {
        this.context = context;
        this.tagList = tagList;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagViewHolder(LayoutInflater.from(context).inflate(R.layout.item_general_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {

        General.Tag tag = tagList.get(position);

        holder.itemView.setVisibility(tag.getIsView() ? View.VISIBLE : View.GONE);
        holder.tag.setText(tag.getName());
    }

    @Override
    public int getItemCount() {
        return tagList == null ? 0 : tagList.size();
    }

    class TagViewHolder extends RecyclerView.ViewHolder {

        TextView tag;

        public TagViewHolder(View itemView) {
            super(itemView);
//            tag = (TextView) itemView.findViewById(R.id.tag);
        }
    }
}
