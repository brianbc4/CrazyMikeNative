package com.crazymike.product.spec.counter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazymike.R;
import com.crazymike.alert.SalesDialog;
import com.crazymike.api.URL;
import com.crazymike.base.BaseActivity;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.Spec;
import com.crazymike.models.SpecCounter;
import com.crazymike.product.spec.selector.SpecSelectorActivity;
import com.crazymike.respositories.SpecCounterRepository;
import com.crazymike.util.DialogTool;
import com.crazymike.web.WebViewActivity;
import com.google.gson.Gson;

import java.util.List;

import static com.crazymike.main.MainActivity.WEB_REQUEST;
import static com.crazymike.product.spec.selector.SpecSelectorActivity.RESULT_CANCEL;
import static com.crazymike.product.spec.selector.SpecSelectorActivity.RESULT_SPEC_SELECTED;

/**
 * Created by ChaoJen on 2017/2/24.
 */

public class SpecCounterActivity
        extends BaseActivity
        implements SpecCounterContract.View, SpecCounterAdapter.Listener, View.OnClickListener {

    private static final String TAG = SpecCounterActivity.class.getSimpleName();

    private static final String ITEM_DETAIL = "ITEM_DETAIL";
    private static final String ITEM_ID = "ITEM_ID";
    public static final int REQUEST_SPEC_SELECT = 1;

    private SpecCounterContract.Presenter mPresenter;

    private RecyclerView mRecyclerViewSpecCounter;
    private SpecCounterAdapter mSpecCounterAdapter;
    private WebView mWebViewForRequest;
    private LinearLayout mLinearLayoutBuyMoreDiscount;
    private TextView mTextViewBuyMoreDiscount;

    public static void startActivity(Context context, ItemDetail itemDetail) {
        Intent intent = new Intent(context, SpecCounterActivity.class);
        intent.putExtra(ITEM_DETAIL, new Gson().toJson(itemDetail));
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String itemId) {
        Intent intent = new Intent(context, SpecCounterActivity.class);
        intent.putExtra(ITEM_ID, itemId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SpecCounterPresenter(this);
        mWebViewForRequest = new WebView(this);

        if (getIntent().getStringExtra(ITEM_DETAIL) != null) {
            onItemDetailGet(new Gson().fromJson(getIntent().getStringExtra(ITEM_DETAIL), ItemDetail.class));
        }
        if (getIntent().getStringExtra(ITEM_ID) != null) {
            mPresenter.getItemDetail(getIntent().getStringExtra(ITEM_ID));
        }
    }

    @Override
    public void onItemDetailGet(ItemDetail itemDetail) {
        mPresenter.setItemDetail(itemDetail);
        initView(itemDetail);
    }

    @Override
    public void initView(ItemDetail itemDetail) {
        setContentView(R.layout.activity_spec_counter);

        mRecyclerViewSpecCounter = (RecyclerView) findViewById(R.id.recycler_spec_counter);
        LinearLayout linearLayoutAddCart = (LinearLayout) findViewById(R.id.linear_layout_add_cart);
        LinearLayout linearLayoutCheckout = (LinearLayout) findViewById(R.id.linear_layout_checkout);
        mLinearLayoutBuyMoreDiscount = (LinearLayout) findViewById(R.id.linearLayout_buyMoreDiscount);
        mTextViewBuyMoreDiscount = (TextView) findViewById(R.id.textView_buyMoreDiscount);

        mSpecCounterAdapter = new SpecCounterAdapter(this, this, mPresenter.getItemDetail());
        mRecyclerViewSpecCounter.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewSpecCounter.setAdapter(mSpecCounterAdapter);
        linearLayoutAddCart.setOnClickListener(this);
        linearLayoutCheckout.setOnClickListener(this);
        mLinearLayoutBuyMoreDiscount.setOnClickListener(this);

        mPresenter.checkSpecListSize();
    }

    @Override
    public void onSpecListSizeChecked(int specListSize) {
        if (specListSize > 1) SpecSelectorActivity.startActivity(this, mPresenter.getItemDetail(), REQUEST_SPEC_SELECT);
    }

    @Override
    public void onSpecCountChanged(List<SpecCounter> specCounterList) {
        mSpecCounterAdapter.notifyDataSetChanged(specCounterList);
        mLinearLayoutBuyMoreDiscount.setVisibility(View.GONE);
        mPresenter.getBuyMoreInfo();
    }

    @Override
    public void onSpecSelectorButtonClick() {
        SpecSelectorActivity.startActivity(this, mPresenter.getItemDetail(), REQUEST_SPEC_SELECT);
    }

    @Override
    public void onIncreaseSpecButtonClick(Spec spec) {
        mPresenter.increaseSpecCount(spec);
    }

    @Override
    public void onDecreaseSpecButtonClick(Spec spec) {
        mPresenter.decreaseSpecCount(spec);
    }

    @Override
    public void onCancelClick() {
        finish();
    }

    @Override
    public void onAddToCart(String item_id, int qty, String specs) {
        //Because api can't get the cookie "cart2" , it never show item in cart web
        //And Server side is XXX always XXX
        //So I just use webView to post the api, and native api to get cart count
        mWebViewForRequest.loadUrl(URL.ADD_CART + "&item_id=" + item_id + "&qty=" + qty + "&specs=" + specs);
    }

    @Override
    public void onBuyMoreDiscountChanged(int buyMore, int buyMoreAverage, String unit) {
        mLinearLayoutBuyMoreDiscount.setVisibility(View.VISIBLE);
        mTextViewBuyMoreDiscount.setText(String.format(getResources().getString(R.string.buy_more_discount), buyMore, unit, buyMoreAverage, unit));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_layout_add_cart:
                checkSelectedSpecs();
                break;
            case R.id.linear_layout_checkout:
                if (checkSelectedSpecs()) {
                    WebViewActivity.startActivity(this, URL.CART, WEB_REQUEST);
                }
                break;
            case R.id.linearLayout_buyMoreDiscount:
                new SalesDialog(this, mPresenter.getItemDetail()).show();
                break;
        }
    }

    private boolean checkSelectedSpecs() {
        if (SpecCounterRepository.getInstance().getProductCount() > 0) {
            mPresenter.addToCart();
            finish();
        } else {
            new DialogTool(this).showHintDialog(String.format(getResources().getString(R.string.no_selected), mPresenter.getItemDetail().getInfo().getUnit()));
//            Toast toast = Toast.makeText(this, String.format(getResources().getString(R.string.no_selected), mPresenter.getItemDetail().getInfo().getUnit()), Toast.LENGTH_LONG);
//            toast.getView().setBackgroundResource(R.color.colorPrimary);
//            toast.getView().setPadding(20, 20, 20, 20);
//            toast.show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_CANCEL:
                if (SpecCounterRepository.getInstance().getProductCount() == 0) {
                    finish();
                }
                break;
            case RESULT_SPEC_SELECTED:
                mRecyclerViewSpecCounter.smoothScrollToPosition(mSpecCounterAdapter.getItemCount());
                break;
        }
    }
}
