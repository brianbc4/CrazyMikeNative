package com.crazymike.notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.crazymike.R;
import com.crazymike.base.BaseActivity;
import com.crazymike.respositories.ProductRepository;

public class NoticeActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, NoticeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_notice);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        toolbar.setNavigationOnClickListener(view -> finish());

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new NoticeAdapter(this, ProductRepository.getInstance().getNotices()));
    }
}
