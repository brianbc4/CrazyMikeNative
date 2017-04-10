package com.crazymike.search.result;

import android.util.Log;

import com.crazymike.trace.writer.BaseWriter;
import com.crazymike.trace.writer.GAWriter;
import com.crazymike.models.ItemList;
import com.crazymike.product.detail.ProductDetailActivity;
import com.crazymike.respositories.CartRepository;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.respositories.ProductRepository;
import com.crazymike.respositories.TrackRepository;
import com.crazymike.trace.sender.GASender;
import com.crazymike.trace.sender.ServerLogSender;
import com.crazymike.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import static com.crazymike.api.DISP.PRICE;
import static com.crazymike.api.DISP.RNDESC;

/**
 * Created by cuber on 2016/11/8.
 */

class SearchQueryPresenter implements SearchQueryContract.Presenter {

    private static final String TAG = SearchQueryPresenter.class.getSimpleName();
    private static final int PAGE_COUNT = 20;

    private SearchQueryContract.View view;
    private int page = 0, sort = -1;
    private List<ItemList> itemLists;
    private boolean isTracking = false;
    private String keyword;

    SearchQueryPresenter(SearchQueryContract.View view) {
        this.view = view;
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

        CartRepository.getInstance().getCartObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(view::onGetCart, Throwable::printStackTrace);

        CartRepository.getInstance().getCartCountObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(view::onGetCartCount, Throwable::printStackTrace);
    }

    @Override
    public void setSortType(int sort) {
        this.sort = sort;
    }

    @Override
    public void setSearchKey(String key) {
        this.keyword = key;
    }

    @Override
    public void searchQuery(boolean needClear) {
        view.showProgress();

        if (needClear) {
            itemLists.clear();
            page = 0;
        }

        ProductRepository.getInstance().searchQuery(keyword, page, PAGE_COUNT, sort == -1 ? RNDESC : PRICE, sort)
                .compose(RxUtil.bindLifecycle(view))
                .compose(RxUtil.mainAsync())
                .doAfterTerminate(() -> view.hideProgress())
                .subscribe(rtn -> {
                    view.onGetCount(rtn.getTotal());
                    itemLists.addAll(rtn.getHtml());
                    view.onGetSearchQuery(itemLists);
                }, throwable -> view.onSearchQueryError(throwable));
    }

    @Override
    public List<Integer> getTrackList() {
        return TrackRepository.getInstance().getTracks();
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

    @Override
    public void scrollBottom() {
        page++;
        searchQuery(false);
    }

    @Override
    public void getCart() {
        CartRepository.getInstance().callCartMap();
    }

    @Override
    public String getLogo() {
        return ProductRepository.getInstance().getChannelInfo().getLogo();
    }

    @Override
    public boolean hasSpecialLogo() {
        return ProductRepository.getInstance().hasSpecialLogo();
    }

    @Override
    public void sendServerLog(String url) {
        ServerLogSender.send(url, BaseWriter.getTargetWithKeyword(keyword));
    }

    @Override
    public void sendGA(String url) {
        GASender.sendScreenView(new GAWriter(url).getSearchMessage(keyword));
    }
}
