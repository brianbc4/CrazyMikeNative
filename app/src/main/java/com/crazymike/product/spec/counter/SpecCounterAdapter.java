package com.crazymike.product.spec.counter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.Spec;
import com.crazymike.models.SpecCounter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChaoJen on 2017/2/24.
 */

public class SpecCounterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SPEC_HEADER = 0;
    private static final int TYPE_SPEC_COUNTER = 1;
    private static final int TYPE_SPEC_SELECTOR = 2;

    private Listener mListener;
    private Context mContext;
    private List<SpecCounter> mSpecCounterList;
    private ItemDetail mItemDetail;

    public SpecCounterAdapter(Context context, Listener listener, ItemDetail itemDetail) {
        mContext = context;
        mListener = listener;
        mItemDetail = itemDetail;

        mSpecCounterList = new ArrayList<>();
    }

    public void notifyDataSetChanged(List<SpecCounter> specCounterList) {
        mSpecCounterList = specCounterList;
        notifyDataSetChanged();
    }

    public interface Listener {

        void onSpecSelectorButtonClick();
        void onIncreaseSpecButtonClick(Spec spec);
        void onDecreaseSpecButtonClick(Spec spec);
        void onCancelClick();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_SPEC_HEADER;
        }
        if (position == getItemCount() - 1) {
            return TYPE_SPEC_SELECTOR;
        }
        return TYPE_SPEC_COUNTER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case TYPE_SPEC_HEADER:
                return new SpecHeaderHolder(inflater.inflate(R.layout.item_product_spec_header, parent, false));
            case TYPE_SPEC_COUNTER:
                return new SpecCounterHolder(inflater.inflate(R.layout.item_product_spec_counter, parent, false));
            case TYPE_SPEC_SELECTOR:
                return new SpecSelectorHolder(inflater.inflate(R.layout.item_product_spec_selector, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_SPEC_HEADER:
                SpecHeaderHolder specHeaderHolder = (SpecHeaderHolder) holder;

                specHeaderHolder.imgCancel.setOnClickListener(view -> mListener.onCancelClick());
                Glide.with(mContext).load(mItemDetail.getInfo().getImg1_url()).into(specHeaderHolder.imgProductPhoto);
                specHeaderHolder.txtProductName.setText(mItemDetail.getInfo().getName());
                specHeaderHolder.txtAverage.setText(String.format("%s $%s /%s", mContext.getResources().getString(R.string.average), getAveragePrice(), mItemDetail.getInfo().getUnit()));
                specHeaderHolder.txtCount.setText(String.format(" x %s", getProductCount()));
                break;
            case TYPE_SPEC_COUNTER:
                SpecCounterHolder specCounterHolder = (SpecCounterHolder) holder;
                Spec currentSpec = mSpecCounterList.get(position - 1).getSpec();

                if (mItemDetail.getSpecs().size() == 1 && currentSpec.getSpec_value().length() == 0)
                    currentSpec.setSpec_value(mContext.getResources().getString(R.string.one_spec));
                specCounterHolder.txtSpecName.setText(currentSpec.getSpec_value());
                specCounterHolder.txtSpecCount.setText(String.valueOf(mSpecCounterList.get(position - 1).getQty()));
                specCounterHolder.lineLayIncrease.setOnClickListener(view -> mListener.onIncreaseSpecButtonClick(currentSpec));
                specCounterHolder.lineLayDecrease.setOnClickListener(view -> {
                    if (mItemDetail.getSpecs().size() == 1 && getProductCount() == 1) return;
                    mListener.onDecreaseSpecButtonClick(currentSpec);
                });
                break;
            case TYPE_SPEC_SELECTOR:
                SpecSelectorHolder specSelectorHolder = (SpecSelectorHolder) holder;

                specSelectorHolder.linearLayoutSpecDelectorRoot.setVisibility(mItemDetail.getSpecs().size() > 1 && getProductCount() < Integer.valueOf(mItemDetail.getInfo().getMax_order()) ? View.VISIBLE : View.GONE);
                specSelectorHolder.btnSpecSelector.setOnClickListener(view -> mListener.onSpecSelectorButtonClick());
                specSelectorHolder.txtTotalPrice.setText(String.format("$%s", getTotalPrice()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSpecCounterList.size() + 2;
    }

    private int getProductCount() {
        int productCount = 0;
        for (SpecCounter specCounter : mSpecCounterList) {
            productCount += specCounter.getQty();
        }
        return productCount;
    }

    private int getAveragePrice() {
        int averagePrice;
        int discount = 0;
        for (int i = 0; i < mItemDetail.getSales().size(); i++) {
            if (getProductCount() >= Integer.valueOf(mItemDetail.getSales().get(i).getQty())) {
                discount = (int) Math.ceil(Double.valueOf(mItemDetail.getSales().get(i).getPrice_discount()));
            }
        }
        averagePrice = Integer.valueOf(mItemDetail.getInfo().getPrice()) - discount;
        return averagePrice;
    }

    private int getTotalPrice() {
        double onePrice = Double.valueOf(mItemDetail.getInfo().getPrice());
        double disCount = 0;
        int totalPrice;
        for (int i = 0; i < mItemDetail.getSales().size(); i++) {
            if (getProductCount() >= Integer.valueOf(mItemDetail.getSales().get(i).getQty())) {
                disCount = Double.valueOf(mItemDetail.getSales().get(i).getPrice_discount());
            }
        }
        onePrice -= disCount;
        totalPrice = (int) Math.round(onePrice * getProductCount());
        return totalPrice;
    }

    private class SpecHeaderHolder extends RecyclerView.ViewHolder {

        private ImageView imgCancel;
        private ImageView imgProductPhoto;
        private TextView txtProductName;
        private TextView txtAverage;
        private TextView txtCount;

        public SpecHeaderHolder(View itemView) {
            super(itemView);

            imgCancel = (ImageView) itemView.findViewById(R.id.image_view_cancel);
            imgProductPhoto = (ImageView) itemView.findViewById(R.id.img_product_photo);
            txtProductName = (TextView) itemView.findViewById(R.id.txt_product_name);
            txtAverage = (TextView) itemView.findViewById(R.id.txt_average);
            txtCount = (TextView) itemView.findViewById(R.id.txt_count);
        }
    }

    private class SpecCounterHolder extends RecyclerView.ViewHolder {

        private TextView txtSpecName;
        private TextView txtSpecCount;
        private LinearLayout lineLayDecrease;
        private LinearLayout lineLayIncrease;

        public SpecCounterHolder(View itemView) {
            super(itemView);

            txtSpecName = (TextView) itemView.findViewById(R.id.txt_spec_title);
            txtSpecCount = (TextView) itemView.findViewById(R.id.txt_spec_count);
            lineLayDecrease = (LinearLayout) itemView.findViewById(R.id.linear_layout_decrease);
            lineLayIncrease = (LinearLayout) itemView.findViewById(R.id.linear_layout_increase);
        }
    }

    private class SpecSelectorHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayoutSpecDelectorRoot;
        private Button btnSpecSelector;
        private TextView txtTotalPrice;

        public SpecSelectorHolder(View itemView) {
            super(itemView);

            linearLayoutSpecDelectorRoot = (LinearLayout) itemView.findViewById(R.id.linear_layout_spec_selector_root);
            btnSpecSelector = (Button) itemView.findViewById(R.id.btn_spec_selector);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.txt_total_price);
        }
    }
}
