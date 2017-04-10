package com.crazymike.respositories;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import rx.Observable;
import rx.Subscriber;

public class CookieRepository {

    public static final String TAG = CookieRepository.class.getSimpleName();
    private static CookieRepository INSTANCE = new CookieRepository();
    private List<Cookie> cookieList;
    private List<String> mktThreeList;
    private boolean isLogin;

    private Observable<CookieRepository> cookieChangeObservable;
    private Subscriber<? super CookieRepository> cookieChangeSubscriber;

    private CookieRepository() {
        cookieList = new ArrayList<>();
        mktThreeList = new ArrayList<>();
    }

    public static CookieRepository getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new CookieRepository());
    }

    public void initial(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
    }

    public Observable<CookieRepository> getCookieObservable() {
        if (cookieChangeObservable == null) {
            cookieChangeObservable = Observable.create((Observable.OnSubscribe<CookieRepository>) subscriber -> cookieChangeSubscriber = subscriber).share();
        }
        return cookieChangeObservable;
    }

    public CookieJar getCookieJar() {
        return new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieList.clear();

                cookieList.add(new Cookie.Builder()
                        .name("channel")
                        .value("app")
                        .domain("appapi2.crazymike.tw")
                        .path("/")
                        .build());

                String cookiesTemp = CookieManager.getInstance().getCookie(url.host());

                if (cookiesTemp != null) {
                    for (String cookie : cookiesTemp.split(";")) {
                        cookieList.add(Cookie.parse(url, cookie));
                    }
                }

                Log.d("Btest","getCookieJar(),size="+cookieList.size());

                isLogin = checkLogin(cookieList);
                if (cookieChangeSubscriber != null) cookieChangeSubscriber.onNext(INSTANCE);


            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                cookieList.clear();

                cookieList.add(new Cookie.Builder()
                        .name("channel")
                        .value("app")
                        .domain("appapi2.crazymike.tw")
                        .path("/")
                        .build());

                String cookies = CookieManager.getInstance().getCookie(url.host());
                if (cookies != null) {
                    for (String cookie : cookies.split(";")) {
                        cookieList.add(Cookie.parse(url, cookie));
                    }
                }

                if (mktThreeList.size() > 0) insertMktThreeCookies();

                Log.d("Btest","loadForRequest() "+ cookieList.size());
                return cookieList;
            }
        };
    }

    private boolean checkLogin(List<Cookie> cookieList) {

        boolean user = false, token = false, sig = false, time = false;

        try {
            for (Cookie cookie : cookieList) {
                if (cookie != null && cookie.name() != null && cookie.name().equals("PHPSESSID") && cookie.value() != null && !cookie.value().equals("")) {
                    PreferencesTool.getInstance().put(PreferencesKey.PHPSESSID, cookie.value());
                }

                if (cookie != null && cookie.name() != null && cookie.name().equals("user") && cookie.value() != null && !cookie.value().equals("")) {
                    user = true;
                    PreferencesTool.getInstance().put(PreferencesKey.LOGIN_USER, cookie.value());
                }

                if (cookie != null && cookie.name() != null && cookie.name().equals("token") && cookie.value() != null && !cookie.value().equals("")) {
                    token = true;
                }

                if (cookie != null && cookie.name() != null && cookie.name().equals("sig") && cookie.value() != null && !cookie.value().equals("")) {
                    sig = true;
                }

                if (cookie != null && cookie.name() != null && cookie.name().equals("time") && cookie.value() != null && !cookie.value().equals("")) {
                    time = true;
                }

                if (cookie != null && cookie.name() != null && cookie.name().equals("member_id") && cookie.value() != null && !cookie.value().equals("")) {
                    PreferencesTool.getInstance().put(PreferencesKey.MEMBER_ID, cookie.value());
                }
            }
            Log.d("Btest","user: "+user +" token: "+token+" sig: "+sig+" time: "+time);

            for (int i = 0; i < cookieList.size(); i++) {
                Log.i("Btest","cookieList:  " + cookieList.get(i).name() + "\t\t" + cookieList.get(i).value());
            }

            return user && token && sig && time;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void clearCookies() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d(TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
                CookieManager.getInstance().removeAllCookies(null);
                CookieManager.getInstance().flush();
            } else {
                Log.d(TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                cookieManager.removeSessionCookie();
                CookieSyncManager.getInstance().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkWebViewCookie(String url) {
        try {
            String cookies = CookieManager.getInstance().getCookie(url);
            String[] cookiesList = cookies.split(";");
            cookieList.clear();
            for (String c : cookiesList) {
                String[] pair = c.split("=");
                cookieList.add(new Cookie.Builder()
                        .name(pair[0].trim())
                        .value(pair[1].trim())
                        .domain("appapi2.crazymike.tw")
                        .path("/")
                        .build());
            }

            Log.d("Btest","checkWebViewCookie()");
            isLogin = checkLogin(cookieList);
            if (cookieChangeSubscriber != null) cookieChangeSubscriber.onNext(INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMktThreeToList(String itemId) {
        for (int i = 0; i < mktThreeList.size(); i++) {
            if (mktThreeList.get(i).equals(itemId)) return;
        }

        mktThreeList.add(itemId);
    }

    private void insertMktThreeCookies() {
        String jsonArray = "{\"72\":[";
        for (int i = 0; i < mktThreeList.size() - 1; i++) {
            jsonArray += "\"" + mktThreeList.get(i) + "\",";
        }
        jsonArray += "\"" + mktThreeList.get(mktThreeList.size() - 1) + "\"]}";

        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).name().equals("mkt3"))
                cookieList.remove(i);
        }

        cookieList.add(new Cookie.Builder()
                .name("mkt3")
                .value(jsonArray)
                .domain("appapi2.crazymike.tw")
                .path("/")
                .build());

        for (int i = 0; i < cookieList.size(); i++) {
            Log.i(TAG, cookieList.get(i).name() + "\t\t" + cookieList.get(i).value());
        }
    }
}