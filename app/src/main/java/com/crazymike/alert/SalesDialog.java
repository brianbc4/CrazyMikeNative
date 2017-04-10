package com.crazymike.alert;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.crazymike.R;
import com.crazymike.models.ItemDetail;
import com.crazymike.product.detail.adapter.SalesAdapter;

/**
 * Created by ChaoJen on 2016/12/6.
 */

public class SalesDialog extends AlertDialog implements SalesAdapter.Listener{

    private RecyclerView mRecyclerViewSales;
    private View mView;

    public SalesDialog(Context context, ItemDetail itemDetail) {
        super(context);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_sales, null);

        setView(mView);
        mRecyclerViewSales = (RecyclerView) mView.findViewById(R.id.recycler_view_sales);
        mRecyclerViewSales.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerViewSales.setAdapter(new SalesAdapter(context, itemDetail, this));
    }

    @Override
    public void onCancelClick() {
        dismiss();
    }
}
