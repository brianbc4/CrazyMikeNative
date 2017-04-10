package com.crazymike.search.result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.api.URL;
import com.crazymike.base.BaseActivity;
import com.crazymike.login.LoginActivity;
import com.crazymike.main.MainActivity;
import com.crazymike.models.Cart;
import com.crazymike.models.ItemList;
import com.crazymike.models.SearchHotKey;
import com.crazymike.product.detail.ProductDetailActivity;
import com.crazymike.product.spec.counter.SpecCounterActivity;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.search.box.SearchDialogFragment;
import com.crazymike.util.ActionName;
import com.crazymike.web.WebExtra;
import com.crazymike.web.WebViewActivity;
import com.crazymike.widget.OnScrollTopAndBottomListener;
import com.crazymike.widget.WrapContentGridLayoutManager;

import java.util.List;
import java.util.Map;

import static com.crazymike.R.id.count;

/**
 * Created by cuber on 2016/11/8.
 */

public class SearchQueryActivity extends BaseActivity implements SearchQueryContract.View, SearchDialogFragment.Listener, OnScrollTopAndBottomListener.OnBottomListener, SearchQueryAdapter.Listener {

    private static final String TAG = SearchQueryActivity.class.getSimpleName();
    public static final String SEARCH_KEY = "SEARCH_KEY";
    private ImageView icon;
    private TextView cartBadge;
    private TextView searchKey;
    private TextView notFound;
    private View notFoundLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchQueryPresenter presenter;
    private SearchQueryAdapter adapter;
    private String key;

    public static void startActivity(Context context, String key) {
        Intent intent = new Intent(context, SearchQueryActivity.class);
        intent.putExtra(SEARCH_KEY, key);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchQueryPresenter(this);
        key = getIntent().getStringExtra(SEARCH_KEY);
        setContentView(R.layout.activity_search_query);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        onCreateMenu(toolbar);
        setSupportActionBar(toolbar);
        toolbar.findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        toolbar.setNavigationOnClickListener(v -> finish());

        icon = (ImageView) findViewById(R.id.icon);
        if (presenter.hasSpecialLogo()) {
            Glide.with(this).load(presenter.getLogo()).into(icon);
        }

        searchKey = (TextView) findViewById(R.id.searchKey);
        searchKey.setText(key);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        notFound = (TextView)findViewById(R.id.notFound);
        notFoundLayout = findViewById(R.id.notFoundLayout);

        //search
        findViewById(R.id.search).setOnClickListener(v -> onActionSearch());
        onCreateSearchBar();

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new WrapContentGridLayoutManager(this, 1));
        recyclerView.addOnScrollListener(new OnScrollTopAndBottomListener().setOnBottomListener(this));
        recyclerView.setAdapter(adapter = new SearchQueryAdapter(this, this));
        adapter.setTrackList(presenter.getTrackList());

        presenter.setSearchKey(key);
        presenter.setSortType(-1);
        presenter.searchQuery(true);

        String url = "";
        try {
            url = getIntent().getStringExtra(WebExtra.URL);
        }catch (Exception e) {
            e.printStackTrace();
        }
        presenter.sendServerLog(url);
        presenter.sendGA(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getCart();
    }

    @Override
    public void onCreateMenu(Toolbar toolbar) {

        //cart
        toolbar.findViewById(R.id.cart).setOnClickListener(view -> onActionCart());
        cartBadge = (TextView) toolbar.findViewById(R.id.cartBadge);
        cartBadge.setVisibility(View.GONE);
    }

    @Override
    public void onCreateSearchBar() {

        View accuracy = findViewById(R.id.accuracy);
        View priceUp = findViewById(R.id.priceUp);
        View priceDown = findViewById(R.id.priceDown);
        accuracy.setEnabled(false);

        View.OnClickListener listener = v -> {

            recyclerView.scrollToPosition(0);
            accuracy.setEnabled(true);
            priceUp.setEnabled(true);
            priceDown.setEnabled(true);
            v.setEnabled(false);

            if (v == accuracy) {
                presenter.setSortType(-1);
            } else if (v == priceUp) {
                presenter.setSortType(0);
            } else if (v == priceDown) {
                presenter.setSortType(1);
            }

            presenter.searchQuery(true);
        };

        accuracy.setOnClickListener(listener);
        priceUp.setOnClickListener(listener);
        priceDown.setOnClickListener(listener);
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
    public void onGetCart(Map<String, Cart> cartMap) {
        cartBadge.setVisibility(cartMap.size() == 0 ? View.GONE : View.VISIBLE);
        cartBadge.setText(String.valueOf(cartMap.size()));
    }

    @Override
    public void onGetCartCount(int count) {
        cartBadge.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        cartBadge.setText(String.valueOf(count));
        Toast toast = Toast.makeText(this, R.string.added_cart, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();
    }

    @Override
    public void onGetCount(int total) {
        ((TextView) findViewById(count)).setText(String.format(getString(R.string.item_count), total));
    }

    @Override
    public void onGetSearchQuery(List<ItemList> html) {
        notFoundLayout.setVisibility(View.GONE);
        if (html.size() == 0) {
            notFoundLayout.setVisibility(View.VISIBLE);
            notFound.setText(String.format(getString(R.string.search_nothing), key));
        } else if (html.size() == 1) {
            ProductDetailActivity.startActivity(this, html.get(0));
            finish();
        } else {
            adapter.setItemList(html);
        }
    }

    @Override
    public void onSearchQueryError(Throwable throwable) {
        new MaterialDialog.Builder(this)
                .title(R.string.no_connection_title)
                .content(R.string.no_connection_content)
                .positiveText(R.string.reload)
                .onPositive((dialog, which) -> onActionReload())
                .negativeText(R.string.close)
                .onNegative((dialog, which) -> finish())
                .show();
    }

    @Override
    public void onTrackAdded() {
        Toast toast = Toast.makeText(this, R.string.added_track, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();
    }

    @Override
    public void onTrackDeleted() {
        Toast toast = Toast.makeText(this, R.string.deleted_track, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();
    }

    @Override
    public void onTrackChanged(List<Integer> tracks) {
        adapter.setTrackList(tracks);
    }

    @Override
    public void onActionReload() {
        presenter.searchQuery(true);
    }

    @Override
    public void onNeedLogin() {
        LoginActivity.startActivity(this);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBottom() {
        presenter.scrollBottom();
    }

    @Override
    public void onClick(ItemList itemList) {
        ProductDetailActivity.startActivity(this, itemList);
        CookieRepository.getInstance().addMktThreeToList(itemList.getItem_id());
    }

    @Override
    public void onCartClick(ItemList itemList) {
        SpecCounterActivity.startActivity(this, itemList.getItem_id());
    }

    @Override
    public void onLikeClick(ItemList itemList) {
        presenter.addTrack(itemList);
    }

    @Override
    public void OnHotKeyClick(SearchHotKey searchHotKey) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(ActionName.SEARCH_KEY);
        intent.putExtra(ActionName.SEARCH_KEY, searchHotKey.getHotkey());
        startActivity(intent);
//        presenter.setSearchKey(searchHotKey.getHotkey());
//        presenter.searchQuery(true);
//        searchKey.setText(searchHotKey.getHotkey());
    }

    @Override
    public void OnGetSearchKey(String search) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(ActionName.SEARCH_KEY);
        intent.putExtra(ActionName.SEARCH_KEY, search);
        startActivity(intent);
//        presenter.setSearchKey(search);
//        presenter.searchQuery(true);
//        searchKey.setText(search);
    }
}
