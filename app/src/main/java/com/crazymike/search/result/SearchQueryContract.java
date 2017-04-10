package com.crazymike.search.result;

import android.support.v7.widget.Toolbar;

import com.crazymike.models.Cart;
import com.crazymike.models.ItemList;

import java.util.List;
import java.util.Map;

/**
 * Created by cuber on 2016/11/8.
 */

interface SearchQueryContract {

    interface View {

        void onCreateMenu(Toolbar toolbar);

        void onCreateSearchBar();

        void onActionCart();

        void onActionSearch();

        void onGetCart(Map<String, Cart> stringCartMap);

        void onGetCartCount(int count);

        void onGetCount(int total);

        void onGetSearchQuery(List<ItemList> html);

        void onSearchQueryError(Throwable throwable);

        void onTrackAdded();

        void onTrackDeleted();

        void onTrackChanged(List<Integer> integers);

        void onActionReload();

        void onNeedLogin();

        void showProgress();

        void hideProgress();

    }

    interface Presenter {

        void setSortType(int sort);

        void setSearchKey(String key);

        void searchQuery(boolean needClear);

        List<Integer> getTrackList();

        void addTrack(ItemList itemList);

        void scrollBottom();

        void getCart();

        String getLogo();

        boolean hasSpecialLogo();

        void sendServerLog(String url);

        void sendGA(String url);
    }
}
