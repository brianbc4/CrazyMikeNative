package com.crazymike.product.list;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.crazymike.R;
import com.crazymike.base.BaseFragment;
import com.crazymike.listener.OnItemSelectedListener;
import com.crazymike.main.MainActivity;
import com.crazymike.models.Banner;
import com.crazymike.models.ItemList;
import com.crazymike.models.RotateJson;
import com.crazymike.models.Tag;
import com.crazymike.product.detail.ProductDetailActivity;
import com.crazymike.product.spec.counter.SpecCounterActivity;
import com.crazymike.product.spec.selector.SpecSelectorActivity;
import com.crazymike.trace.sender.UserBehaviorSender;
import com.crazymike.web.WebViewActivity;
import com.crazymike.widget.OnScrollTopAndBottomListener;
import com.crazymike.widget.WrapContentGridLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends BaseFragment implements ProductListContract.View, ProductAdapter.OnItemClickListener, OnScrollTopAndBottomListener.OnBottomListener, OnScrollTopAndBottomListener.OnUpScrollListener, OnScrollTopAndBottomListener.OnDownScrollListener {

    public static final String TAG = "ProductListFragment";
    private static final String ARG_TAG = "ARG_TAG";

    private Listener listener;
    private String tagId, subTag1;

    private ProductAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private Button top;
    private Spinner subTag1Spinner, subTag2Spinner;
    private RecyclerView recyclerView;
    private View noConnection, tagLayout;
    private ProductListPresenter presenter;

    public interface Listener {
        void needLogin();

        void showWebView(String item_url);

        void onUpScroll();

        void onDownScroll();
    }

    public String getGATag() {
        if (tagId.equals("5")) return "channel-5";
        return "tag-" + tagId;
    }

    public void setTagID(String tag) {
        this.tagId = tag;
        recyclerView.scrollToPosition(0);
        presenter.setTag(tag);
    }

    public String getTagId() {
        return tagId;
    }

    public static ProductListFragment newInstance(String tag) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TAG, tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagId = getArguments().getString(ARG_TAG, "");
        presenter = new ProductListPresenter(this, tagId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        noConnection = view.findViewById(R.id.noConnection);
        Button reload = (Button) view.findViewById(R.id.reload);
        reload.setOnClickListener(v -> {
            presenter.getItemList(true);
            noConnection.setVisibility(View.GONE);
        });

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        top = (Button) view.findViewById(R.id.top);
        top.setOnClickListener(v -> onTopCLick());

        tagLayout = view.findViewById(R.id.tagLayout);
        subTag1Spinner = (Spinner) view.findViewById(R.id.parentTagSpinner);
        subTag2Spinner = (Spinner) view.findViewById(R.id.childTagSpinner);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.swipeRefresh());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new OnScrollTopAndBottomListener().setOnBottomListener(this).setOnUpScrollListener(20, this).setOnDownScrollListener(30, this));
        adapter = new ProductAdapter(activity, gridLayoutManager, presenter.getBanner(), new ArrayList<>(), tagId, presenter.getTrackList(), this);
        adapter.setIsTravelTag(presenter.isTravelTag());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noConnection.setVisibility(View.GONE);
        presenter.setTag(tagId);
        presenter.getBanner(tagId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getArguments().putString(ARG_TAG, tagId);
    }

    @Override
    public void onBottom() {
        presenter.scrollBottom();
    }

    @Override
    public void onUpScroll() {
        top.setVisibility(View.VISIBLE);
        listener.onUpScroll();
    }

    @Override
    public void onDownScroll() {
        top.setVisibility(View.GONE);
        listener.onDownScroll();
    }

    @Override
    public void onTopCLick() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onGetBanner(List<Banner> bannerList) {
        adapter.setBanners(bannerList);
    }


    @Override
    public void onGetItemList(List<ItemList> itemLists) {
        adapter.setBanners(presenter.getBanner());
        adapter.setItemList(itemLists);
    }

    @Override
    public void onGetItemListError(Throwable throwable) {
        noConnection.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrackAdded() {
        Toast toast = Toast.makeText(context, R.string.added_track, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();
    }

    @Override
    public void onTrackDeleted() {
        Toast toast = Toast.makeText(context, R.string.deleted_track, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.color.colorPrimary);
        toast.getView().setPadding(20, 20, 20, 20);
        toast.show();
    }

    @Override
    public void onTrackChanged(List<Integer> tracks) {
        adapter.setTrackList(tracks);
    }

    @Override
    public void onNeedLogin() {
        listener.needLogin();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onGetTag(Tag tag1, int tagPosition) {

        tagLayout.setVisibility(View.VISIBLE);
        List<Tag> tags = tag1.getSubs();

        String[] item = new String[tags.size() + 1];
        item[0] = getString(R.string.choice_child_tag);
        for (int i = 0; i < tags.size(); i++) {
            item[i + 1] = tags.get(i).getName();
        }

        subTag1Spinner.setVisibility(tags.size() == 0 ? View.GONE : View.VISIBLE);
        subTag1Spinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_tag, item));
        subTag1Spinner.setOnItemSelectedListener(new OnItemSelectedListener(position -> onSubTag1Selected(position, tags)));
    }

    @Override
    public void onGetSubTag1(Tag subTag1, int subTag1Position) {

        tagLayout.setVisibility(View.VISIBLE);
        List<Tag> tags = subTag1.getSubs();

        String[] item = new String[tags.size() + 1];
        item[0] = getString(R.string.choice_child_tag);
        for (int i = 0; i < tags.size(); i++) {
            item[i + 1] = tags.get(i).getName();
        }

        subTag2Spinner.setVisibility(tags.size() == 0 ? View.GONE : View.VISIBLE);
        subTag2Spinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_tag, item));
        subTag2Spinner.setOnItemSelectedListener(new OnItemSelectedListener(position -> onSubTag2Selected(position, tags)));
        subTag1Spinner.setSelection(subTag1Position + 1);
    }

    @Override
    public void onGetSubTag2(Tag subTag2, int subTag2Position) {
        subTag2Spinner.setSelection(subTag2Position + 1);
    }

    private void onSubTag1Selected(int position, List<Tag> tags) {
        tagLayout.setVisibility(View.GONE);
        subTag1Spinner.setVisibility(View.GONE);
        subTag2Spinner.setVisibility(View.GONE);
        recyclerView.scrollToPosition(0);
        presenter.setTag(position == 0 ? this.tagId : (subTag1 = tags.get(position - 1).getTag_id()));
    }

    private void onSubTag2Selected(int position, List<Tag> tags) {
        tagLayout.setVisibility(View.GONE);
        subTag1Spinner.setVisibility(View.GONE);
        subTag2Spinner.setVisibility(View.GONE);
        recyclerView.scrollToPosition(0);
        presenter.setTag(position == 0 ? subTag1 : tags.get(position - 1).getTag_id());
    }

    @Override
    public void onClick(ItemList itemList) {
        if (!tagId.equals("")) {
            ProductDetailActivity.startActivity(context, itemList);
            new UserBehaviorSender().sendItemListClick(itemList, tagId);
        } else {
            listener.showWebView(itemList.getItem_url());
        }
    }

    @Override
    public void onCartClick(ItemList itemList) {
        SpecCounterActivity.startActivity(context, itemList.getItem_id());
    }

    @Override
    public void onLikeClick(ItemList itemList) {
        presenter.addTrack(itemList);
    }

    @Override
    public void onBannerClick(Banner banner, RotateJson rotateJson) {
        WebViewActivity.startActivity(activity, rotateJson.getUrl(), MainActivity.WEB_REQUEST);
        new UserBehaviorSender().sendBannerClick(banner, rotateJson);
    }
}
