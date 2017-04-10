package com.crazymike.product.spec.selector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.Spec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChaoJen on 2017/2/22.
 */

public class SpecSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SPEC = 0;
    private static final String TAG = SpecSelectorAdapter.class.getSimpleName();

    private Context mContext;
    private List<Spec> mSpecList;
    private Listener mListener;

    public SpecSelectorAdapter(Context context, ItemDetail itemDetail, Listener listener) {
        mSpecList = new ArrayList<>();
        mContext = context;
        mListener = listener;

        for (Spec spec : itemDetail.getSpecs()) {
            if (spec.getSpec_stock().equals("0") && spec.getIs_show_sale_out().equals("f") || (itemDetail.getSpecs().size() == 1 && spec.getSpec_value().length() == 0)) {

            } else {
                mSpecList.add(spec);
            }
        }
    }

    public interface Listener {
        void onSpecClick(Spec spec);
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_SPEC;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case VIEW_TYPE_SPEC:
                return new SpecViewHolder(inflater.inflate(R.layout.item_product_spec, parent, false));
        }
        return new SpecViewHolder(inflater.inflate(R.layout.item_product_spec, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SPEC:
                SpecViewHolder specViewHolder = (SpecViewHolder) holder;
                Spec currentSpec = mSpecList.get(position);
                boolean isSpecStockOut = currentSpec.getSpec_stock().equals("0");
                boolean isShowSaleOut = currentSpec.getIs_show_sale_out().equals("t");

                specViewHolder.txtSpecTitle.setText(currentSpec.getSpec_value());
                specViewHolder.lineLayRoot.setAlpha((float) (isSpecStockOut && isShowSaleOut ? 0.3 : 1));
                specViewHolder.txtSaleOut.setVisibility(isSpecStockOut && isShowSaleOut ? View.VISIBLE : View.GONE);

                specViewHolder.lineLayRoot.setOnClickListener(
                        v -> {
                            if (!isSpecStockOut) {
                                mListener.onSpecClick(currentSpec);
                            }
                        });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSpecList.size();
    }

    private class SpecViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lineLayRoot;
        private TextView txtSpecTitle;
        private TextView txtSaleOut;

        public SpecViewHolder(View itemView) {
            super(itemView);

            lineLayRoot = (LinearLayout) itemView.findViewById(R.id.lineLay_root);
            txtSpecTitle = (TextView) itemView.findViewById(R.id.txt_spec_title);
            txtSaleOut = (TextView) itemView.findViewById(R.id.txt_sale_out);
        }
    }
}
