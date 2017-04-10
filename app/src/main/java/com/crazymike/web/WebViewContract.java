package com.crazymike.web;

import com.crazymike.models.Tag;

public interface WebViewContract {

    interface View{

        void onBackClick();

        void toProduct(String itemUrl);

        void onToSpecialUrl(String url);

        void onShowAlert(String msg);

        void onToTagWebView(String url);

        void onToTagPage(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position);

        void onToHomePage(String tagId);

        void onBackButtonHide();

        void toSearch(String keyWord);

        void onLoginFailure();

    }

    interface Presenter{

        boolean isNeedCheck(String url);

        boolean checkUrl(String url);

        void checkCookie(String url);

        void handleHTML(String html);

        void checkBackButtonNeedHide(String url);
    }
}
