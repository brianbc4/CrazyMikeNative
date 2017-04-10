package com.crazymike.product.detail;

import android.support.v7.widget.Toolbar;

import com.crazymike.models.Cart;
import com.crazymike.models.General;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;

import java.util.List;
import java.util.Map;

interface ProductDetailContract {

    interface View {

        void onCreateMenu(Toolbar toolbar);

       void onActionCart();

        void onActionSearch();

        void onGetItemDetail(ItemDetail itemDetail);

        void onGetPromote(General general);

        void onGetItemDetailError(Throwable throwable);

        void onGetPromoteError(Throwable throwable);

        void onGetCart(Map<String, Cart> cartMap);

        void onGetCartCount(Integer count);

        void onTrackChanged(boolean b);

        void onTimerCountDown(int sec);

        void onGetItemList(List<ItemList> itemLists);

        void showProgress();

        void hideProgress();

        void showWebView(String item_url);

        void onOrderNoticeChecked(boolean isNoticeOrder);

        void onOrderNoticeRequestUrlGet(boolean isComplete, String orderNoticeRequestUrl);

        void onCSServiceMessageResponseGet(boolean isSuccess, String message);
    }

    interface Presenter {

        void getItemDetail(String itemId);

        void getPromote(String itemId);

        void getCart();

        String getBuyNewUrl(String itemId);

        boolean isLogin();

        boolean isTrack(String itemId);

        void addTrack(String itemId);

        void buy(ItemDetail itemDetail);

        void countDown(String date_online);

        void stopTimer();

        void getItemList();

        void handleItemListResponse(List<ItemList> items);

        void onScrollBottom();

        void sendServerLog(String itemId, String url);

        void sendGA(String url, String itemId);

        boolean hasSpecialLogo();

        String getLogo();

        boolean isTravelTag(String tagId);

        void checkOrderNotice();

        void getOrderNoticeRequestUrl();

        void getOrderNoticeRequestUrlOnce(String orderNoticeType, String orderNoticeSend);

        String getCSServiceStartTime();

        String getCSServiceEndTime();

        void postCSServiceMessage(String name, String title, String phoneNumber, String email, String content);
    }
}
