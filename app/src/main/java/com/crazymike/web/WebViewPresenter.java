package com.crazymike.web;

import android.util.Log;

import com.crazymike.api.URL;
import com.crazymike.models.Tag;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.respositories.ProductRepository;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;

import java.util.ArrayList;
import java.util.List;

class WebViewPresenter implements WebViewContract.Presenter {

    private static final String TAG = WebViewPresenter.class.getSimpleName();

    private WebViewContract.View view;
    private List<String> notNeedCheckUrl;

    WebViewPresenter(WebViewContract.View view) {
        this.view = view;
    }

    @Override
    public boolean isNeedCheck(String url) {
        if (notNeedCheckUrl == null) notNeedCheckUrl = new ArrayList<>();
        for (String u : notNeedCheckUrl) {
            if (url.equals(u)) return false;
        }
        return true;
    }

    @Override
    public boolean checkUrl(String url) {

        if (WebRule.isWeb(url)) return false;

        String msg = WebRule.isShowAlert(url);
        if (msg != null) {
            view.onShowAlert(msg);
        }

        if (WebRule.isClickBack(url)) {
            view.onBackClick();
            return true;
        }

        String itemUrl = WebRule.isProduct(url);
        if (itemUrl != null) {
            view.toProduct(itemUrl);
            return true;
        }

        String tag = WebRule.isTag(url);
        if (tag != null) {
            checkTagIsInUpsideMenu(tag, url);
        }

        if (WebRule.isSpecialUrl(url)) {
            view.onToSpecialUrl(url);
            return true;
        }

        String keyWord = WebRule.isSearch(url);
        if (keyWord != null) {
            view.toSearch(keyWord);
            return true;
        }

        return false;
    }

    @Override
    public void checkCookie(String url) {
        CookieRepository.getInstance().checkWebViewCookie(url);
    }

    @Override
    public void checkBackButtonNeedHide(String url) {
        List<String> needHideButtonUrls = new ArrayList<>();
        needHideButtonUrls.add(URL.CART);
        needHideButtonUrls.add(URL.GUEST_ORDER_LIST);
        needHideButtonUrls.add(URL.ORDER_LIST);
        needHideButtonUrls.add(URL.BUY);
        needHideButtonUrls.add(URL.BONUS_GIFT);
        needHideButtonUrls.add(URL.BONUS_MEMBER);
        needHideButtonUrls.add(URL.CONTRACT);
        needHideButtonUrls.add(URL.CONTRACT_LIST);
        needHideButtonUrls.add(URL.USER_MENU);

        for(String needHideButtonUrl : needHideButtonUrls){
            if(url.equals(needHideButtonUrl)){
                view.onBackButtonHide();
                return;
            }
        }
    }

    private void checkTagIsInUpsideMenu(String tagId, String url) {

        ProductRepository.getInstance().getSubTag(tagId, new ProductRepository.OnGetSubTagCallback() {
            @Override
            public void getSubTag(Tag tag, int tagPosition, Tag subTag1, int subTag1Position, Tag subTag2, int subTag2Position) {
                view.onToTagPage(tag, tagPosition, subTag1, subTag1Position, subTag2, subTag2Position);
            }

            @Override
            public void getNothing() {
//                notNeedCheckUrl.add(url);
//                view.onToTagPage();
                view.onToHomePage(tagId);
            }
        });
    }


    @Override
    public void handleHTML(String html) {

        //<script>appLogin(true, "aKbUI3CaZlB3wKGb10ehFDXHdidJaETtVIlQ59UuVyg.", "crazy", "J4EkzLOftvrOCVsy8ieRX8sjFyotP-4VUKDi3kEf4Bc.");</script>
        if (!html.contains("appLogin")) {
            return;
        }

        String data = html.replace("<html><head><meta http-equiv=\"CACHE-CONTROL\" content=\"NO-CACHE\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><script>appLogin(", "")
                .replace(");</script></body></html>", "");
        //true, "aKbUI3CaZlB3wKGb10ehFDXHdidJaETtVIlQ59UuVyg.", "crazy", "J4EkzLOftvrOCVsy8ieRX8sjFyotP-4VUKDi3kEf4Bc."

        Log.d("Btest","data: "+data);

        String success = data.split(",")[0];
        if (!success.equals("true")) {
            view.onLoginFailure();
            return;
        }

        try {
            String memberId = data.split(",")[1].replace("\"", "").replace(" ", "");
            String type = data.split(",")[2].replace("\"", "").replace(" ", "");
            String user = data.split(",")[3].replace("\"", "").replace(" ", "");

            PreferencesTool.getInstance().put(PreferencesKey.MEMBER_ID, memberId);
            PreferencesTool.getInstance().put(PreferencesKey.LOGIN_TYPE, type);
            PreferencesTool.getInstance().put(PreferencesKey.LOGIN_USER, user);

            CookieRepository.getInstance().setIsLogin(true);

        } catch (Exception e) {
            e.printStackTrace();
            view.onLoginFailure();
        }
    }

}
