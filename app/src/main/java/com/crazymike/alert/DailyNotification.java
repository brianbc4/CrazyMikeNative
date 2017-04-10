package com.crazymike.alert;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.api.NetworkService;
import com.crazymike.api.response.DailyNoticeResponse;
import com.crazymike.models.DailyNotice;
import com.crazymike.util.RxUtil;
import com.crazymike.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 2017/3/16.
 */

public class DailyNotification extends AlertDialog implements View.OnClickListener{

    private Context mContext;
    private View view;
    private Button conferm;
    private ImageView close;
    private TextView title,content;

    private DailyNotice dailyNotice;


    public DailyNotification(Context context) {
        super(context);
        mContext = context;

        initView();
    }

    private void initView(){
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_daily_notice, null);

        close = (ImageView) view.findViewById(R.id.close);
        title = (TextView) view.findViewById(R.id.title);
        content = (TextView) view.findViewById(R.id.content);
        conferm = (Button) view.findViewById(R.id.conferm);

        NetworkService.getInstance().getDailyNoticeApi().callDailyNotice("notice","-1","android","indextAlert")
                .map(dailyNoticeResponse -> {
                    dailyNotice= dailyNoticeResponse.getDailyNoticeList().get(0);
                    title.setText(dailyNotice.getTitle());
                    content.setText(dailyNotice.getContent());
                    conferm.setText(dailyNotice.getIs_url().equals("t")?"立即前往":"好的");
                    return this;
                }).compose(RxUtil.mainAsync())
                .subscribe();

        setView(view);

        close.setOnClickListener(this);
        conferm.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.close:
            dismiss();
                break;
            case R.id.conferm:
                if(dailyNotice.getIs_url().equals("t")){
                    WebViewActivity.startActivity(mContext,dailyNotice.getUrl());
                    dismiss();
                }else{
                    dismiss();
                }
                break;
        }
        }
}