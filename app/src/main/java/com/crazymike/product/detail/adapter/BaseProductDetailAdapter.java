package com.crazymike.product.detail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crazymike.R;
import com.crazymike.models.General;
import com.crazymike.models.Info;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.models.Online;
import com.crazymike.util.Util;
import com.crazymike.web.WebViewActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ChaoJen on 2016/12/1.
 */

public abstract class BaseProductDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TOP_BANNER = 0;
    protected static final int TYPE_TITLE = 1;
    protected static final int TYPE_PRICE = 2;
    protected static final int TYPE_COUNT_DOWN = 3;
    protected static final int TYPE_IMAGE = 4;
    protected static final int TYPE_SECOND = 5;
    protected static final int TYPE_PRODUCT_INFO = 6;
    protected static final int TYPE_SALES = 7;
    protected static final int BOTTOM_BANNER = 8;
    protected static final int TYPE_ITEM_HEADER = 9;
    protected static final int TYPE_ITEM_LIST = 10;

    protected final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm", Locale.TAIWAN);

    protected String downDiscount;
    protected Context context;
    protected Listener event;
    protected ItemDetail itemDetail;
    protected General general;
    protected boolean isTrack;
    protected boolean isTravelTag;
    protected Info info;
    protected Online online;
    protected Date date;
    protected String discount;
    protected String discountString, discString, notaxString, priceFakeString, dollarsString, bonusAmtString, favorString, surplusString, hourString, minuteString, secondString;
    protected List<ItemList> items;
    protected boolean mIsNoticeOrder;

    public abstract void setIsOrderNotice(boolean isNoticeOrder);

    public interface Listener {
        void onAddToCartClick();

        void onBuyClick();

        void onTrackClick();

        void onShareBtnClick();

        void onBannerClick(General general);

        void onItemListClick(ItemList itemList);

        void onPageSelected();

        void onOrderNoticeBtnClick();

        void onSalesInfoBtnClick(ItemDetail itemDetail);

        void onSpecialistAdviceBtnClick(String productId);
    }

    protected void init(Context context) {

        downDiscount = context.getString(R.string.down_discount);
        discString = context.getString(R.string.disc);
        notaxString = context.getString(R.string.notax);
        priceFakeString = context.getString(R.string.price_fake);
        dollarsString = context.getString(R.string.dollars);
        discountString = context.getString(R.string.discount);
        favorString = context.getString(R.string.favor);
        bonusAmtString = context.getString(R.string.bonus_amt);
        surplusString = context.getString(R.string.surplus);
        hourString = context.getString(R.string.hour);
        minuteString = context.getString(R.string.minute);
        secondString = context.getString(R.string.second);

        date = new Date();
        items = new ArrayList<>();
    }

    public void setItemDetail(ItemDetail itemDetail) {
        this.itemDetail = itemDetail;
        info = itemDetail.getInfo();
        online = itemDetail.getOnline();
        discount = Util.getDiscount(info.getPrice_fake(), info.getPrice());
        notifyDataSetChanged();
    }

    public void setIsTravelTag(boolean isTravelTag) {
        this.isTravelTag = isTravelTag;
    }

    public BaseProductDetailAdapter setGeneral(General general) {
        this.general = general;
        notifyDataSetChanged();
        return this;
    }

    public void setTrack(boolean b) {
        this.isTrack = b;
        notifyDataSetChanged();
    }

    public abstract void setCountDownTime(int sec);

    public void setItemList(List<ItemList> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    protected long getTime(String date) {
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isNoticeOrder() {
        return mIsNoticeOrder;
    }

    protected void settingWebView(WebView webView, String html) {

        if (html == null || html.equals("")) {
            webView.setVisibility(View.GONE);
            return;
        }

        if (webView.getTag() != null && webView.getTag().equals(html)) {
            return;
        }

        String head = "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";
        String closedTag = "</body></html>";
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadDataWithBaseURL(null, head + html + closedTag, "text/html", "UTF-8", null);
        Log.d("btest",head + html + closedTag);
        webView.setTag(html);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebViewActivity.startActivity(context, url);
                return true;
            }
        });
    }

    protected void settingYoutubeWebView(WebView webView, String url) {

        if (url == null || url.equals("")) {
            webView.setVisibility(View.GONE);
            return;
        }

        if (webView.getTag() != null && webView.getTag().equals(url)) {
            return;
        }

        String head = "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";
        String closedTag = "</body></html>";
        String youtubeUrl = String.format("<iframe src=\"%s\" frameborder=\"0\" allowfullscreen></iframe>", url);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadDataWithBaseURL(null, head + youtubeUrl + closedTag, "text/html", "UTF-8", null);
        webView.setTag(url);
    }
}
