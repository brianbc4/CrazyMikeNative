package com.crazymike.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.crazymike.CrazyMike;
import com.crazymike.R;
import com.crazymike.alert.DailyNotification;
import com.crazymike.alert.TerminalServiceTimeDialog;
import com.crazymike.api.NetworkService;
import com.crazymike.api.URL;
import com.crazymike.listener.OnPageChangedListener;
import com.crazymike.login.LoginActivity;
import com.crazymike.main.base.MainBaseActivity;
import com.crazymike.models.Cart;
import com.crazymike.models.LeftSideMenu;
import com.crazymike.models.Promote;
import com.crazymike.models.SearchHotKey;
import com.crazymike.models.Tag;
import com.crazymike.models.UpSideMenu;
import com.crazymike.notice.NoticeActivity;
import com.crazymike.product.list.ProductListFragment;
import com.crazymike.search.box.SearchDialogFragment;
import com.crazymike.search.result.SearchQueryActivity;
import com.crazymike.util.ActionName;
import com.crazymike.util.EmptyFragment;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.web.WebExtra;
import com.crazymike.web.WebViewActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends MainBaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View, ProductListFragment.Listener, SearchDialogFragment.Listener, TagListAdapter.Listener {

    public static final String TAG = "MainActivity";

    public static final String PUSH = "PUSH";
    public static final String MSG_ID = "MSG_ID";
    public static final String HYPER_LINK = "HYPER_LINK";

    public static final int WEB_REQUEST = 0x02;

    private ImageView icon;
    private LinearLayout linearLayoutTagView;
    private ViewPager viewPager;
    private TextView noticeBadge;
    private TextView cartBadge;
    private View splash;
    private RecyclerView tagRecycler;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private UpSideMenuPagerAdapter adapter;
    private LinearLayout linearLayoutTerminal;

    private MainPresenter presenter;

    private int pagePosition;

    /**
     * For NavigationBar
     */
    public static void startActivity(Context context, String msgId, String hyperLink) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(PUSH);
        intent.putExtra(MSG_ID, msgId);
        intent.putExtra(HYPER_LINK, hyperLink);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);

        /* Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        onCreateMenu(toolbar);

        icon = (ImageView) findViewById(R.id.icon);

        /* cart cartBadge */
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_cart);
        if (menuItem != null && menuItem.getActionView() != null) {
            View cartView = menuItem.getActionView();
            cartBadge = (TextView) cartView.findViewById(R.id.badge);
            cartView.setOnClickListener(v -> onActionCart());
        }

        /* NavigationView */
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* Drawer */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /* Version */
        ((TextView) findViewById(R.id.version)).setText(PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class) ?
                String.format("%s %s %s", getString(R.string.version), CrazyMike.version(), getString(R.string.devel_site)) :String.format("%s %s", getString(R.string.version), CrazyMike.version()));

        /* ViewPager */
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter = new UpSideMenuPagerAdapter(getSupportFragmentManager(), new ArrayList<>()));
        viewPager.addOnPageChangeListener(new OnPageChangedListener(position -> {
            Fragment fragment = adapter.getRegisteredFragment(position);

            if (fragment instanceof ProductListFragment) {
                String url = "";
                try {
                    url = getIntent().getStringExtra(WebExtra.URL);
                }catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                presenter.sendServerLog(url, ((ProductListFragment) fragment).getTagId());
                presenter.sendGA(url, position);
                presenter.sendUserBehaviorTrace(position);
                this.pagePosition = position;
            } else if (fragment instanceof EmptyFragment) {
                showWebView(adapter.getMenu(position).getUrl());
            }
        }));

        /* TabLayout */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                linearLayoutTagView.setVisibility(View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    ((ProductListFragment) adapter.getRegisteredFragment(tab.getPosition())).setTagID("5");
                }
            }
        });

        /* Tag */
        View tag = findViewById(R.id.tag);
        tag.setOnClickListener(this::onTagClick);
        linearLayoutTagView = (LinearLayout) findViewById(R.id.tagList);

        tagRecycler = (RecyclerView) findViewById(R.id.tagRecycler);
        tagRecycler.setLayoutManager(new GridLayoutManager(this, 3));

        View bottomBanner = findViewById(R.id.bottomBanner);
        bottomBanner.setOnClickListener(v -> WebViewActivity.startActivity(this, URL.BOTTOM_BANNER));
        View bottomBannerClose = findViewById(R.id.bottomBannerClose);
        bottomBannerClose.setOnClickListener(v -> bottomBanner.setVisibility(View.GONE));

        //search
        findViewById(R.id.search).setOnClickListener(view -> onActionSearch());

        //splash
        splash = findViewById(R.id.splash);
        splash.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }

        // terminal
        linearLayoutTerminal = (LinearLayout) findViewById(R.id.lineLay_terminal);
        linearLayoutTerminal.setVisibility(PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class) ? View.VISIBLE : View.GONE);

        LinearLayout linearLayoutGcmToken = (LinearLayout) findViewById(R.id.linearLayout_gcmToken);
        linearLayoutGcmToken.setOnClickListener(
                view -> {
                    String strGcmToken = PreferencesTool.getInstance().get(PreferencesKey.GCM_TOKE, String.class);
                    startActivity(Intent.createChooser(
                            new Intent().setAction(Intent.ACTION_SEND)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_TEXT, strGcmToken), "GCM_TOKEN"));
        });

        LinearLayout linearLayoutServiceTime = (LinearLayout) findViewById(R.id.linearLayout_serviceTime);
        linearLayoutServiceTime.setOnClickListener(
                view -> {
                    new TerminalServiceTimeDialog(this).show();
                }
        );

        presenter.callAppIndex();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getCart();
        presenter.getTrackList();
        onLoginStateChange(presenter.isLogin());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(ActionName.SEARCH_KEY)) {
            OnGetSearchKey(intent.getStringExtra(ActionName.SEARCH_KEY));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (data != null) {

            switch (data.getAction()) {

                case ActionName.TAG:
                    onActionTagClick(data.getStringExtra(ActionName.TAG));
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        ProductListFragment productListFragment = ((ProductListFragment) adapter.getRegisteredFragment(pagePosition));

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (pagePosition == 0 && productListFragment != null && !productListFragment.getTagId().equals("5")) {
            productListFragment.setTagID("5");

        } else {
            new MaterialDialog.Builder(this)
                    .content(R.string.dialog_finish_content)
                    .positiveText(R.string.dialog_finish_positive)
                    .onPositive((dialog, which) -> finish())
                    .negativeText(R.string.dialog_finish_negative)
                    .show();
        }
    }

    @Override
    public void onCreateMenu(Toolbar toolbar) {

        //menu
        toolbar.findViewById(R.id.menu).setOnClickListener(view -> drawer.openDrawer(GravityCompat.START));

        //notice
        toolbar.findViewById(R.id.notice).setOnClickListener(view -> onActionNotice());
        noticeBadge = (TextView) toolbar.findViewById(R.id.noticeBadge);

        //cart
        toolbar.findViewById(R.id.cart).setOnClickListener(view -> onActionCart());
        cartBadge = (TextView) toolbar.findViewById(R.id.cartBadge);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(getResources().getString(R.string.home))) {
            viewPager.setCurrentItem(0);
            ((ProductListFragment) adapter.getRegisteredFragment(0)).setTagID("5");
        }else if (item.getTitle().equals(getResources().getString(R.string.login_here))) {
            LoginActivity.startActivity(this);
        }else if (item.getTitle().equals(getResources().getString(R.string.logout))) {
            presenter.logout();
        }else {
            showWebView(presenter.getLeftSideMenuUrl(item));
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onActionNotice() {
        noticeBadge.setVisibility(View.GONE);
        NoticeActivity.startActivity(this);
    }

    @Override
    public void onActionCart() {
        showWebView(URL.CART);
    }

    @Override
    public void onActionSearch() {
        SearchDialogFragment.newInstance().show(getSupportFragmentManager());
    }

    @Override
    public boolean onActionTagClick(String tagId) {
        presenter.checkTagId(tagId);
        return false;
    }

    @Override
    public void onLoginStateChange(boolean isLogin) {
        presenter.callAppLeftSideMenu(isLogin);
    }

    @Override
    public void onTagClick(View view) {
        TagListAdapter tagListAdapter = new TagListAdapter(presenter.getTag(), presenter.getTagImage(), this);
        tagRecycler.setAdapter(tagListAdapter);
        linearLayoutTagView.setVisibility(linearLayoutTagView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onGetUpSideMenu(List<UpSideMenu> upSideMenus) {
        splash.setVisibility(View.GONE);
        viewPager.setOffscreenPageLimit(upSideMenus.size());
        adapter.notifyDataSetChanged(upSideMenus);
        presenter.getDailyNotification(String.valueOf(DateTime.now().toDateTime()).split("T")[0]);
    }

    @Override
    public void onGetPromote(Promote promote) {
//
//        General general = promote.getGeneral().get(0);
//        StringBuilder builder = new StringBuilder();
//        builder.append(String.format(getString(R.string.promote_content), general.getPassword(), general.getDiscPromote()));
//        if (general.getIsMember()) {
//            builder.append(String.format("(%s)", getString(R.string.only_member)));
//        }
//
//        new MaterialDialog.Builder(this)
//                .title(R.string.news_title)
//                .content(builder.toString())
//                .positiveText(R.string.yes)
//                .show();
    }

    @Override
    public void onGetCart(Map<String, Cart> cartMap) {
        cartBadge.setVisibility(cartMap.size() == 0 ? View.GONE : View.VISIBLE);
        cartBadge.setText(String.valueOf(cartMap.size()));
    }

    @Override
    public void onGetCartCount(Integer count) {
        cartBadge.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        cartBadge.setText(String.valueOf(count));
        Toast toast = Toast.makeText(this, R.string.added_cart, Toast.LENGTH_LONG);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();
    }

    @Override
    public void onConnectionError() {
        new MaterialDialog.Builder(this)
                .cancelable(false)
                .title(R.string.no_connection_title)
                .content(R.string.no_connection_content)
                .positiveText(R.string.reload)
                .onPositive((dialog, which) -> presenter.callAppIndex())
                .negativeText(R.string.close)
                .onNegative((dialog1, which1) -> finish())
                .show();
    }

    @Override
    public void onGetTag(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position) {
        List<UpSideMenu> upSideMenus = presenter.getUpSideMenus();

        for (int i = 0; i < upSideMenus.size(); i++) {
            if (upSideMenus.get(i).getUrl().contains(tag.getTag_id())) {
                pagePosition = i;
                viewPager.setCurrentItem(pagePosition);
            }
        }

        String tagId = subTag2 != null ? subTag2.getTag_id() : subTag1 != null ? subTag1.getTag_id() : tag.getTag_id();
        ((ProductListFragment) adapter.getRegisteredFragment(pagePosition)).setTagID(tagId);
    }

    @Override
    public void startSearchQuery(String search) {
        SearchQueryActivity.startActivity(this, search);
    }

    @Override
    public void onShowSpecialTagInHomePage(String tagId) {
        viewPager.setCurrentItem(0);
        ((ProductListFragment) adapter.getRegisteredFragment(pagePosition)).setTagID(tagId);
    }

    @Override
    public void onGetLaunchIcon(String logo) {
        Glide.with(this).load(logo).into(icon);
    }

    @Override
    public void onGetLeftSideMenu(List<LeftSideMenu> leftSideMenus) {
        navigationView.getMenu().clear();
        for (int index = 0; index < leftSideMenus.size(); index++) {
            navigationView.getMenu().add(0, index, index, leftSideMenus.get(index).getName());
        }
    }

    @Override
    public void needLogin() {
        LoginActivity.startActivity(this);
    }

    @Override
    public void OnHotKeyClick(SearchHotKey searchHotKey) {
        presenter.checkTagName(searchHotKey.getHotkey());
    }

    @Override
    public void OnGetSearchKey(String search) {
        if (search.equals(getResources().getString(R.string.devel_site_key))) {
            NetworkService.getInstance().changeSide();
            finish();
            startActivity(this, "", "");
            return;
        }
        presenter.checkTagName(search);
    }

    @Override
    public void showWebView(String item_url) {
        WebViewActivity.startActivity(this, item_url, WEB_REQUEST);
    }

    @Override
    public void onUpScroll() {
        linearLayoutTerminal.setVisibility(PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDownScroll() {
        linearLayoutTerminal.setVisibility(View.GONE);
    }

    @Override
    public void onTagClick(String tagId) {
        presenter.checkTagId(tagId);
        linearLayoutTagView.setVisibility(View.GONE);
    }

    @Override
    public void showDailyNotification(){
        new DailyNotification(this).show();
    }
}
