package com.crazymike.main;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.models.Tag;

import java.util.List;

/**
 * Created by user1 on 2017/2/21.
 */

public class TagListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tag> mTagList;
    private List<Integer> mTagImageList;
    private Listener mListener;

    public TagListAdapter(List<Tag> tagList, List<Integer> tagImageList, Listener listener) {
        mListener = listener;
        mTagList = tagList;
        mTagImageList = tagImageList;
    }

    public interface Listener {
        void onTagClick(String tagId);
    }

    @Override
    public TagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_tag_view,null);

        TagHolder rcv = new TagHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TagHolder tagHolder = (TagHolder) holder;

        tagHolder.txtTagName.setText(mTagList.get(position).getName());
        tagHolder.imgTagPhoto.setImageResource(mTagImageList.get(position));
        tagHolder.imgTagPhoto.setOnClickListener(view -> mListener.onTagClick(mTagList.get(position).getTag_id()));
    }

    @Override
    public int getItemCount() {
        return this.mTagList.size();
    }

    private class TagHolder extends RecyclerView.ViewHolder {

        public TextView txtTagName;
        public ImageView imgTagPhoto;

        public TagHolder(View itemView) {

            super(itemView);
            txtTagName = (TextView)itemView.findViewById(R.id.country_name);
            imgTagPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
        }
    }
}