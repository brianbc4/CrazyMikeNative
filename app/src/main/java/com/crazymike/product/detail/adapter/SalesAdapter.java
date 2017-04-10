package com.crazymike.product.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.Sale;

/**
 * Created by ChaoJen on 2016/12/5.
 */
public class SalesAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_SALES_TITLE = 0;
    private static final int VIEW_TYPE_BUY_ONE = 1;
    private static final int VIEW_TYPE_BUY_MORE = 2;
    private static final int VIEW_TYPE_MOST_DISCOUNT = 3;

    private Context context;

    private ItemDetail itemDetail;
    private String salesTitle;
    private int miniOrder;
    private String unit;
    private int price;
    private String buyOneUnitFormat;
    private String priceFormat;
    private String buyOneDiscount;
    private String buyMoreUnitFormat;
    private String mostDiscountUnitFormat;
    private boolean isDialog;
    private Listener listener;

    public SalesAdapter(Context context, ItemDetail itemDetail) {
        this.context = context;

        this.itemDetail = itemDetail;

        this.salesTitle = context.getResources().getString(R.string.sales_title);
        this.miniOrder = Integer.valueOf(itemDetail.getInfo().getMini_order());
        this.unit = itemDetail.getInfo().getUnit();
        this.price = Integer.valueOf(itemDetail.getInfo().getPrice());
        this.buyOneUnitFormat = context.getResources().getString(R.string.format_buy_one_unit);
        this.priceFormat = context.getResources().getString(R.string.format_buy_one_price);
        this.buyOneDiscount = context.getResources().getString(R.string.format_buy_one_discount);
        this.buyMoreUnitFormat = context.getResources().getString(R.string.format_buy_more_unit);
        this.mostDiscountUnitFormat = context.getResources().getString(R.string.format_most_discount_unit);
        isDialog = false;
    }

    //as dialog constructor
    public SalesAdapter(Context context, ItemDetail itemDetail, Listener listener) {
        this(context, itemDetail);
        this.listener = listener;
        this.isDialog = true;
    }

    public interface Listener {
        void onCancelClick();
    }

    @Override
    public int getItemCount() {
        if (!isDialog && itemDetail.getSales().size() > 3) return 5;
        return itemDetail.getSales().size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_SALES_TITLE;
        }
        if (position == 1) {
            return VIEW_TYPE_BUY_ONE;
        }
        if (position == itemDetail.getSales().size() + 1) {
            if(itemDetail.getSales().size() > 4 && !isDialog) return VIEW_TYPE_BUY_MORE;
            return VIEW_TYPE_MOST_DISCOUNT;
        }
        return VIEW_TYPE_BUY_MORE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_SALES_TITLE:
                return new SalesTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_title, parent, false));
            case VIEW_TYPE_BUY_ONE:
                return new BuyMoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_buy_more, parent, false));
            case VIEW_TYPE_BUY_MORE:
                return new BuyMoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_buy_more, parent, false));
            case VIEW_TYPE_MOST_DISCOUNT:
                return new MostDiscountHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_most_discount, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Sale sale;
        int forwardQty;
        int afterwardQty;
        int priceDiscount;
        int average;

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SALES_TITLE:
                SalesTitleHolder salesTitleHolder = (SalesTitleHolder) holder;

                salesTitleHolder.textViewTitle.setText(salesTitle);
                salesTitleHolder.imageViewCancel.setVisibility(isDialog ? View.VISIBLE : View.GONE);
                salesTitleHolder.imageViewCancel.setOnClickListener(view -> listener.onCancelClick());
                break;
            case VIEW_TYPE_BUY_ONE:
                BuyMoreHolder buyOneHolder = (BuyMoreHolder) holder;

                buyOneHolder.textViewQty.setText(String.format(buyOneUnitFormat, miniOrder, unit));
                buyOneHolder.textViewAverage.setText(String.format(priceFormat, price));
                buyOneHolder.textViewAverage.setTextColor(context.getResources().getColor(R.color.text_black_none_special));
                buyOneHolder.textViewUnit.setText(String.format("/%s", unit));
                buyOneHolder.textViewSaveNow.setText(buyOneDiscount);
                buyOneHolder.textViewDiscount.setVisibility(View.GONE);
                break;
            case VIEW_TYPE_MOST_DISCOUNT:
                MostDiscountHolder mostDiscountHolder = (MostDiscountHolder) holder;

                sale = itemDetail.getSales().get(position - 2);
                forwardQty = Integer.valueOf(sale.getQty());
                priceDiscount = (int) Math.ceil(Double.valueOf(sale.getPrice_discount()));
                average = price - priceDiscount;

                mostDiscountHolder.textViewQty.setText(String.format(mostDiscountUnitFormat, forwardQty, unit));
                mostDiscountHolder.textViewAverage.setText(String.format(priceFormat, average));
                mostDiscountHolder.textViewUnit.setText(String.format("/%s", unit));
                mostDiscountHolder.textViewDiscount.setText(String.format(priceFormat, priceDiscount));
                break;
            case VIEW_TYPE_BUY_MORE:
                BuyMoreHolder buyMoreHolder = (BuyMoreHolder) holder;

                sale = itemDetail.getSales().get(position - 2);
                forwardQty = Integer.valueOf(sale.getQty());
                afterwardQty = Integer.valueOf(itemDetail.getSales().get(position - 1).getQty()) - 1;
                priceDiscount = (int) Math.ceil(Double.valueOf(sale.getPrice_discount()));
                average = price - priceDiscount;

                buyMoreHolder.textViewQty.setText(String.format(buyMoreUnitFormat, forwardQty, afterwardQty, unit));
                buyMoreHolder.textViewAverage.setText(String.format(priceFormat, average));
                buyMoreHolder.textViewAverage.setTextColor(context.getResources().getColor(R.color.text_price_product));
                buyMoreHolder.textViewUnit.setText(String.format("/%s", unit));
                buyMoreHolder.textViewDiscount.setText(String.format(priceFormat, priceDiscount));
        }
    }

    class SalesTitleHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private ImageView imageViewCancel;

        public SalesTitleHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.text_view_title);
            imageViewCancel = (ImageView) itemView.findViewById(R.id.image_view_cancel);
        }
    }

    class BuyMoreHolder extends RecyclerView.ViewHolder {

        private TextView textViewQty;
        private TextView textViewAverage;
        private TextView textViewUnit;
        private TextView textViewSaveNow;
        private TextView textViewDiscount;

        public BuyMoreHolder(View itemView) {
            super(itemView);
            textViewQty = (TextView) itemView.findViewById(R.id.text_view_qty);
            textViewAverage = (TextView) itemView.findViewById(R.id.text_view_average);
            textViewUnit = (TextView) itemView.findViewById(R.id.text_view_unit);
            textViewSaveNow = (TextView) itemView.findViewById(R.id.text_view_save_now);
            textViewDiscount = (TextView) itemView.findViewById(R.id.text_view_price_discount);
        }
    }

    class MostDiscountHolder extends RecyclerView.ViewHolder {
        private TextView textViewQty;
        private TextView textViewAverage;
        private TextView textViewUnit;
        private TextView textViewDiscount;
        public MostDiscountHolder(View itemView) {
            super(itemView);
            textViewQty = (TextView) itemView.findViewById(R.id.text_view_qty);
            textViewAverage = (TextView) itemView.findViewById(R.id.text_view_average);
            textViewUnit = (TextView) itemView.findViewById(R.id.text_view_unit);
            textViewDiscount = (TextView) itemView.findViewById(R.id.text_view_price_discount);
        }
    }
}
