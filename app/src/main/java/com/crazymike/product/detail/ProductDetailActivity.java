package com.crazymike.product.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.alert.OrderNoticeDialog;
import com.crazymike.alert.SalesDialog;
import com.crazymike.alert.SpecialistAdviceDialog;
import com.crazymike.alert.SpecialistAdviceMessageDialog;
import com.crazymike.api.URL;
import com.crazymike.base.BaseActivity;
import com.crazymike.login.LoginActivity;
import com.crazymike.main.MainActivity;
import com.crazymike.models.Cart;
import com.crazymike.models.General;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.models.ProductBanner;
import com.crazymike.models.SearchHotKey;
import com.crazymike.product.detail.adapter.BaseProductDetailAdapter;
import com.crazymike.product.detail.adapter.TravelProductDetailAdapter;
import com.crazymike.product.spec.counter.SpecCounterActivity;
import com.crazymike.search.box.SearchDialogFragment;
import com.crazymike.search.result.SearchQueryActivity;
import com.crazymike.util.ActionName;
import com.crazymike.util.DialogTool;
import com.crazymike.web.WebViewActivity;
import com.crazymike.widget.OnScrollTopAndBottomListener;
import com.crazymike.widget.WrapContentGridLayoutManager;

import java.util.List;
import java.util.Map;

import static com.crazymike.main.MainActivity.WEB_REQUEST;

public class ProductDetailActivity
        extends BaseActivity
        implements ProductDetailContract.View, BaseProductDetailAdapter.Listener, SearchDialogFragment.Listener, OrderNoticeDialog.Listener, SpecialistAdviceDialog.Listener, SpecialistAdviceMessageDialog.Listener {

    private static final String TAG = ProductDetailActivity.class.getSimpleName();
    private static final String ITEM_ID = "ITEM_ID";
    private static final String ITEM_URL = "ITEM_URL";
    private static final int REQUEST_PERMISSION_CALL_PHONE = 1;

    private static String mStrItemURL;

    private ProductDetailPresenter mPresenter;
    private String mStrItemId;
    private BaseProductDetailAdapter mAdapter;
    private TextView mTxtCartBadge;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mManager;
    private ItemDetail mItemDetail;
    private AlertDialog mSpecialistAdviceMessageDialog;
    private AlertDialog mSpecialistAdviceDialog;
    private String mTempPhoneNumber;

    public static void startActivity(Context context, ItemList itemList) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(ITEM_ID, itemList.getItem_id());
        intent.putExtra(ITEM_URL, itemList.getItem_url());
        context.startActivity(intent);
    }

    public static void startActivityForBanner(Context context, ProductBanner itemInfo) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        String url= itemInfo.getHref_link();
        intent.putExtra(ITEM_ID, url.split("item-")[1]);
        intent.putExtra(ITEM_URL, url);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String itemUrl) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        try {
            intent.putExtra(ITEM_ID, itemUrl.split("item-")[1].split("\\?")[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra(ITEM_URL, itemUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProductDetailPresenter(this);

        // Data
        mStrItemId = getIntent().getStringExtra(ITEM_ID);
        mStrItemURL = getIntent().getStringExtra(ITEM_URL);

        // View
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        onCreateMenu(toolbar);
        setSupportActionBar(toolbar);
        toolbar.findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        toolbar.setNavigationOnClickListener(view -> finish());

        ImageView imgIcon = (ImageView) findViewById(R.id.icon);
        if (mPresenter.hasSpecialLogo()) {
            Glide.with(this).load(mPresenter.getLogo()).into(imgIcon);
        }

        // RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addOnScrollListener(new OnScrollTopAndBottomListener().setOnBottomListener(() -> mPresenter.onScrollBottom()));
        mManager = new WrapContentGridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mManager);

        // search
        findViewById(R.id.search).setOnClickListener(view -> onActionSearch());

        mPresenter.getItemDetail(mStrItemId);
        mPresenter.sendServerLog(mStrItemId, mStrItemURL);
        mPresenter.sendGA(mStrItemURL, mStrItemId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getPromote(mStrItemId);
        mPresenter.getCart();
    }

    @Override
    public void onCreateMenu(Toolbar toolbar) {
        //cart
        toolbar.findViewById(R.id.cart).setOnClickListener(view -> onActionCart());
        mTxtCartBadge = (TextView) toolbar.findViewById(R.id.cartBadge);
        mTxtCartBadge.setVisibility(View.GONE);
    }

    @Override
    public void onActionCart() {
        WebViewActivity.startActivity(this, URL.CART);
    }

    @Override
    public void onActionSearch() {
        SearchDialogFragment.newInstance().show(getSupportFragmentManager());
    }

    @Override
    public void onGetItemDetail(ItemDetail itemDetail) {
        this.mItemDetail = itemDetail;
        mAdapter = mPresenter.isTravelTag(itemDetail.getInfo().getMain_tag()) ?
                new TravelProductDetailAdapter(this, this, mManager, null, mPresenter.isTrack(mStrItemId)) :
                new TravelProductDetailAdapter(this, this, mManager, null, mPresenter.isTrack(mStrItemId));
        mAdapter.setItemDetail(itemDetail);
        mAdapter.setIsTravelTag(mPresenter.isTravelTag(itemDetail.getInfo().getMain_tag()));
        mPresenter.countDown(itemDetail.getOnline().getDate_offline());
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.checkOrderNotice();
    }

    @Override
    public void onGetPromote(General general) {
        mAdapter.setGeneral(general);
    }

    @Override
    public void onGetItemDetailError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onGetPromoteError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onGetCart(Map<String, Cart> cartMap) {
        mTxtCartBadge.setVisibility(cartMap.size() == 0 ? View.GONE : View.VISIBLE);
        mTxtCartBadge.setText(String.valueOf(cartMap.size()));
    }

    @Override
    public void onGetCartCount(Integer count) {
        mTxtCartBadge.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        mTxtCartBadge.setText(String.valueOf(count));
        Toast toast = Toast.makeText(this, R.string.added_cart, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();
    }

    @Override
    public void onTrackChanged(boolean isTrack) {
        Toast toast = Toast.makeText(this, isTrack ? R.string.added_track : R.string.deleted_track, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();
        mAdapter.setTrack(isTrack);
    }

    @Override
    public void onTimerCountDown(int sec) {
        mAdapter.setCountDownTime(sec);
    }

    @Override
    public void onGetItemList(List<ItemList> itemLists) {
        mAdapter.setItemList(itemLists);
    }

    @Override
    public void showProgress() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void onAddToCartClick() {
        SpecCounterActivity.startActivity(this, mItemDetail);
    }

    @Override
    public void onBuyClick() {
        WebViewActivity.startActivity(this, mPresenter.getBuyNewUrl(mStrItemId));
    }

    @Override
    public void onTrackClick() {
        if (mPresenter.isLogin()) {
            mPresenter.addTrack(mStrItemId);
        } else {
            LoginActivity.startActivity(this);
        }
    }

    @Override
    public void onShareBtnClick() {
        String content = mPresenter.getItemName() + "\n" + mStrItemURL;
        startActivity(Intent.createChooser(
                new Intent().setAction(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, content),
                mPresenter.getItemName()));
    }

    @Override
    public void onItemListClick(ItemList itemList) {
        ProductDetailActivity.startActivity(this, itemList);
    }

    @Override
    public void onPageSelected() {
        mRecyclerView.scrollToPosition(3);
    }

    @Override
    public void onOrderNoticeBtnClick() {
        mPresenter.getOrderNoticeRequestUrl();
    }

    @Override
    public void onSalesInfoBtnClick(ItemDetail itemDetail) {
        new SalesDialog(this, itemDetail).show();
    }

    @Override
    public void onSpecialistAdviceBtnClick(String productId) {
        mSpecialistAdviceDialog = new SpecialistAdviceDialog(this, this, productId, mPresenter.isTravelTag(mItemDetail.getInfo().getMain_tag()), mPresenter.getCSServiceStartTime(), mPresenter.getCSServiceEndTime());
        mSpecialistAdviceDialog.show();
    }

    @Override
    public void onSpecialistAdviceMessageBtnClick() {
        mSpecialistAdviceMessageDialog = new SpecialistAdviceMessageDialog(this, this, mItemDetail.getInfo().getName(), mItemDetail.getInfo().getItem_id());
        mSpecialistAdviceMessageDialog.show();
    }

    @Override
    public void onBannerClick(General general) {

//        MaterialDialog dialog = new MaterialDialog.Builder(this)
//                .customView(R.layout.dialog_promote, false)
//                .build();
//
//        TextView content = (TextView) dialog.findViewById(R.id.content);
//        StringBuilder sb = new StringBuilder();
//
//        if (general.getPassword() != null && general.getPassword().size() > 0) {
//            sb.append(getString(R.string.checkout_input));
//            sb.append(getString(R.string.promote_password));
//            sb.append(",");
//            ((BannerHolder) holder).password.setVisibility(View.VISIBLE);
//            ((BannerHolder) holder).password.setText(general.getPassword().get(0));
//            ((BannerHolder) holder).type.setText(R.string.checkout_input);
//        } else {
//            ((BannerHolder) holder).password.setVisibility(View.GONE);
//        }
//
//        switch (general.getDiscType()) {
//            case 1:
//                sb.
//                ((BannerHolder) holder).content.setText(String.format(context.getString(R.string.promote_content_1), general.getDiscPromote()));
//                ((BannerHolder) holder).type.setText(context.getString(R.string.promote_type_1));
//                break;
//
//            case 2:
//                ((BannerHolder) holder).content.setText(String.format(context.getString(R.string.promote_content_2), general.getDiscCash().get(0), general.getDiscCash().get(1)));
//                ((BannerHolder) holder).type.setText(context.getString(R.string.promote_type_2));
//                break;
//
//            case 3:
//                ((BannerHolder) holder).content.setText(String.format(context.getString(R.string.promote_content_3), general.getDiscCash()));
//                ((BannerHolder) holder).type.setText(context.getString(R.string.promote_type_3));
//                break;
//        }
//
//
//
//        ((BannerHolder) holder).itemView.setOnClickListener(view -> event.onBannerClick(general));
//
//
//        //password
//        View passwordLayout = dialog.findViewById(R.id.passwordLayout);
//
//
//        if (general.getPassword() != null && general.getPassword().size() > 0 && general.getIsMember()) {
//            passwordLayout.setVisibility(View.VISIBLE);
//            ((TextView) dialog.findViewById(R.id.password)).setText(general.getPassword().get(0));
//            dialog.findViewById(R.id.copy).setOnClickListener(view -> {
//
//            });
//        } else {
//            passwordLayout.setVisibility(View.GONE);
//        }
//
//        content.setText(String.format(getString(R.string.promote_content_1), general.getDiscPromote()));
//        //content
//        StringBuilder builder = new StringBuilder();
//        builder.append(String.format(getString(R.string.promote_content), general.getPassword().get(0), general.getDiscPromote()));
//        if (general.getIsMember()) {
//            builder.append(String.format("(%s)", getString(R.string.only_member)));
//        }
//
//        ((TextView) dialog.findViewById(R.id.offDate)).setText(general.getDateOffline());
//        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setAdapter(new GeneralTagAdapter(this, general.getTag()));
//        dialog.show();
    }

    @Override
    public void OnHotKeyClick(SearchHotKey searchHotKey) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(ActionName.SEARCH_KEY);
        intent.putExtra(ActionName.SEARCH_KEY, searchHotKey.getHotkey());
        startActivity(intent);
    }

    @Override
    public void OnGetSearchKey(String search) {
        SearchQueryActivity.startActivity(this, search);
    }

    @Override
    public void showWebView(String item_url) {
        WebViewActivity.startActivity(this, item_url, WEB_REQUEST);
    }

    @Override
    public void onOrderNoticeChecked(boolean isNoticeOrder) {
        mAdapter.setIsOrderNotice(isNoticeOrder);
    }

    @Override
    public void onOrderNoticeRequestUrlGet(boolean isComplete, String orderNoticeRequestUrl) {
        if (!mAdapter.isNoticeOrder()) {
            if (isComplete) {
                new WebView(this).loadUrl(orderNoticeRequestUrl);
                mAdapter.setIsOrderNotice(true);
                new DialogTool(this).showHintDialog(getResources().getString(R.string.order_notice_content));
            } else {
                new OrderNoticeDialog(this, this).show();
            }
        } else {
            new DialogTool(this).showHintDialog(getResources().getString(R.string.order_notice_content));
        }
    }

    @Override
    public void onCSServiceMessageResponseGet(boolean isSuccess, String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();

        if (isSuccess) {
            if (mSpecialistAdviceMessageDialog.isShowing()) {
                mSpecialistAdviceMessageDialog.dismiss();
            }
            if (mSpecialistAdviceDialog.isShowing()) {
                mSpecialistAdviceDialog.dismiss();
            }
        }
    }

    @Override
    public void onOrderNoticeDialogSendBtnClick(String orderNoticeType, String orderNoticeSend) {
        mPresenter.getOrderNoticeRequestUrlOnce(orderNoticeType, orderNoticeSend);
    }

    @Override
    public void onSpecialistAdviceCallNowClick(String phoneNumber) {
        callPhone(phoneNumber);
    }

    public void callPhone(String phoneNumber) {
        mTempPhoneNumber = phoneNumber;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CALL_PHONE
            }, REQUEST_PERMISSION_CALL_PHONE);
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    @Override
    public void onSendBtnClick(String name, String title, String phoneNumber, String email, String content) {
        mPresenter.postCSServiceMessage(name, title, phoneNumber, email, content);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; ++i) {
            if (permissions[i].equals(Manifest.permission.CALL_PHONE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                callPhone(mTempPhoneNumber);
            }
        }
    }
}
