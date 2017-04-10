package com.crazymike.product.list;

import com.crazymike.models.Banner;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.models.Tag;
import com.crazymike.respositories.CartRepository;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.respositories.ProductRepository;
import com.crazymike.respositories.TrackRepository;
import com.crazymike.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

class ProductListPresenter implements ProductListContract.Presenter {

    private ProductListContract.View view;
    private ProductRepository productRepository;

    private String tag;
    private int page = 1;
    private List<ItemList> itemLists;
    private List<Banner> bannerList;
    private boolean isTracking = false;

    ProductListPresenter(ProductListContract.View view, String tag) {
        this.view = view;
        this.tag = tag;
        this.productRepository = ProductRepository.getInstance();
        this.itemLists = new ArrayList<>();

        TrackRepository.getInstance().getTrackAddOrDelObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(aBoolean -> {
                    if (!isTracking) return;
                    isTracking = false;
                    if (aBoolean) view.onTrackAdded();
                    else view.onTrackDeleted();
                }, Throwable::printStackTrace);

        TrackRepository.getInstance().getTrackChangedObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(view::onTrackChanged, Throwable::printStackTrace);
    }

    @Override
    public List<ItemList> getItemList() {
        return itemLists;
    }

    @Override
    public ProductListPresenter setTag(String tagId) {
        if (tagId.equals("")) return this;

        this.tag = tagId;
        this.page = 1;

        ProductRepository.getInstance().getSubTag(tagId, new ProductRepository.OnGetSubTagCallback() {
            @Override
            public void getSubTag(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position) {
                if (tag != null) view.onGetTag(tag, tagPosition);
                if (subTag1 != null) view.onGetSubTag1(subTag1, subTag1Position);
                if (subTag2 != null) view.onGetSubTag2(subTag2, subTag2Position);
            }

            @Override
            public void getNothing() {

            }
        });

        getItemList(true);
        return this;
    }

    @Override
    public List<Banner> getBanner() {
        return tag.equals("5")?ProductRepository.getInstance().getBanners():bannerList;
    }

    @Override
    public boolean isTravelTag() {
        return ProductRepository.getInstance().checkIsTravelTag(tag);
    }

    @Override
    public void getBanner(String tag) {
        ProductRepository.getInstance().callBanner(tag)
                .compose(RxUtil.mainAsync())
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(banner -> {
                    List<Banner> banners = new ArrayList<>();
                    banners.add(banner);
                    this.bannerList = banners;
                    view.onGetBanner(banners);
                }, Throwable::printStackTrace);
    }

    @Override
    public void getItemList(boolean needClear) {
        view.showProgress();

        if (needClear) itemLists.clear();

        Observable<List<ItemList>> listObservable;
        if (!tag.equals("5")) {
            listObservable = productRepository.callItemListByTag("5", tag, page);
        } else {
            listObservable = productRepository.callItemListByChannel(tag, page);
        }

        listObservable.compose(RxUtil.mainAsync())
                .compose(RxUtil.bindLifecycle(view))
                .doAfterTerminate(() -> view.hideProgress())
                .subscribe(this::handleItemListResponse, throwable -> view.onGetItemListError(throwable));
    }

    @Override
    public List<Integer> getTrackList() {
        return TrackRepository.getInstance().getTracks();
    }

    @Override
    public void handleItemListResponse(List<ItemList> items) {
        itemLists.addAll(items);
        view.onGetItemList(itemLists);
    }

    @Override
    public void scrollBottom() {
        page++;
        getItemList(false);
    }

    @Override
    public void swipeRefresh() {
        page = 1;
        getItemList(true);
    }

    @Override
    public void addTrack(ItemList itemList) {

        if (!CookieRepository.getInstance().isLogin()) {
            view.onNeedLogin();
            return;
        }

        isTracking = true;
        if (TrackRepository.getInstance().isIn(itemList.getItem_id())) {
            TrackRepository.getInstance().delTrack(itemList.getItem_id());
        } else {
            TrackRepository.getInstance().addTrack(itemList.getItem_id());
        }
    }
}
