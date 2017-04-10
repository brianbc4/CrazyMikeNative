package com.crazymike.main;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crazymike.models.Cart;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.LeftSideMenu;
import com.crazymike.models.Promote;
import com.crazymike.models.Tag;
import com.crazymike.models.UpSideMenu;

import java.util.List;
import java.util.Map;

public interface MainContract {
    interface View {

        void onCreateMenu(Toolbar toolbar);

        void onActionNotice();

        void onActionCart();

        void onActionSearch();

        boolean onActionTagClick(String tagId);

        void onLoginStateChange(boolean isLogin);

        void onTagClick(android.view.View view);

        void onGetUpSideMenu(List<UpSideMenu> upSideMenus);

        void onGetPromote(Promote promote);

        void onGetCart(Map<String, Cart> cartMap);

        void onGetCartCount(Integer count);

        void onConnectionError();

        void onGetTag(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position);

        void startSearchQuery(String search);

        void onShowSpecialTagInHomePage(String tag);

        void onGetLaunchIcon(String logo);

        void onGetLeftSideMenu(List<LeftSideMenu> leftSideMenus);

        void showDailyNotification();
    }

    interface Presenter {


        void callAppIndex();

        void callAppLeftSideMenu(boolean isLogin);

        List<UpSideMenu> getUpSideMenus();

        List<Tag> getTag();

        void getCart();

        void getTrackList();

        void logout();

        void sendUserBehaviorTrace(int position);

        void sendServerLog(String url, String tagId);

        void sendGA(String url, int position);

        boolean isLogin();

        void checkTagName(String tagName);

        void checkTagId(String tagId);

        void getDailyNotification(String day);

        String getLeftSideMenuUrl(MenuItem item);
    }
}
