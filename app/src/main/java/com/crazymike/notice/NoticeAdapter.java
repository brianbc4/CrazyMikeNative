package com.crazymike.notice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.Notice;
import com.crazymike.web.WebViewActivity;

import java.util.List;

class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private Context context;
    private List<Notice> notices;

    NoticeAdapter(Context context, List<Notice> notices) {
        this.context = context;
        this.notices = notices;
    }

    @Override
    public int getItemCount() {
        return notices == null ? 0 : notices.size();
    }

    @Override
    public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new NoticeViewHolder(layoutInflater.inflate(R.layout.item_notice, parent, false));
    }

    @Override
    public void onBindViewHolder(NoticeViewHolder holder, int position) {

        Notice notice = notices.get(position);
        Glide.with(context).load(notice.getImg()).into(holder.img);
        holder.title.setText(notice.getTitle());
        holder.date.setText(notice.getDate_on());
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView title;
        TextView date;

        NoticeViewHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            itemView.setOnClickListener(view -> {
                Notice notice = notices.get(getAdapterPosition());
                WebViewActivity.startActivity(context, notice.getUrl());
            });
        }
    }
}
