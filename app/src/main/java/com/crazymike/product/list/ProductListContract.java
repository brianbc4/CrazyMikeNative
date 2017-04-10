package com.crazymike.product.list;

import com.crazymike.models.Banner;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.models.Tag;

import java.util.List;

public interface ProductListContract {

    interface View {

        void onTopCLick();

        void onGetBanner(List<Banner> bannerList);

        void onGetItemList(List<ItemList> itemLists);

        void onGetItemListError(Throwable throwable);

        void onTrackAdded();

        void onTrackDeleted();

        void onTrackChanged(List<Integer> integers);

        void onNeedLogin();

        void showProgress();

        void hideProgress();

        void onGetTag(Tag tag1, int tagPosition);

        void onGetSubTag1(Tag subTag1, int subTag1Position);

        void onGetSubTag2(Tag subTag2, int subTag2Position);
    }

    interface Presenter {

        List<ItemList>  getItemList();

        ProductListPresenter setTag(String tag);

        List<Banner> getBanner();

        boolean isTravelTag();

        void getBanner(String tag);

        void getItemList(boolean needClear);

        List<Integer> getTrackList();

        void handleItemListResponse(List<ItemList> items);

        void scrollBottom();

        void swipeRefresh();

        void addTrack(ItemList itemList);
    }
}
