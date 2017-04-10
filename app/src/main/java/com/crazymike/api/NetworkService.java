package com.crazymike.api;

import android.util.Log;
import android.webkit.CookieManager;

import com.crazymike.api.service.Api;
import com.crazymike.api.service.AppApi2;
import com.crazymike.api.service.CartApi;
import com.crazymike.api.service.DailyNoticeApi;
import com.crazymike.api.service.DevelAppApi2;
import com.crazymike.api.service.PromoteAPi;
import com.crazymike.api.service.ServerApi;
import com.crazymike.api.service.TraceApi;
import com.crazymike.api.service.TrackApi;
import com.crazymike.respositories.CookieRepository;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    public static final String TAG = "NetworkService";
    public static String CDN;
    public static String OPEN_API;

    private static NetworkService INSTANCE;
    private final static int TIME_OUT_CONNECT = 30;
    private final static int TIME_OUT_READ = 30;
    private final static int TIME_OUT_WRITE = 30;

    public final Api api;
    private final CartApi cartApi;
    private final PromoteAPi promoteApi;
    private final AppApi2 appApi2;
    public DevelAppApi2 develAppApi2;
    private final TrackApi trackApi;
    private final TraceApi traceApi;
    private final ServerApi serverApi;
    private final DailyNoticeApi dailyNoticeApi;

    public static NetworkService getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new NetworkService());
    }

    public static void resetInstance() {
        INSTANCE = null;
    }

    private NetworkService() {

        if (PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class)) {
            CDN = URL.BASE_CDN_DEVEL;
            OPEN_API = URL.BASE_OPEN_API_DEVEL;
        } else {
            CDN = URL.BASE_CDN;
            OPEN_API = URL.BASE_OPEN_API;
        }

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.getCookie(CDN);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            if (isJsonValid(message)) {}
            else Log.i(TAG, message);
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(TIME_OUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_WRITE, TimeUnit.SECONDS)
                .cookieJar(CookieRepository.getInstance().getCookieJar())
                .build();

        Retrofit retrofitCDN = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(NonJsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(CDN)
                .client(client)
                .build();

        Retrofit retrofitOpenApi = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(NonJsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(OPEN_API)
                .client(client)
                .build();

        api = retrofitCDN.create(Api.class);
        cartApi = retrofitCDN.create(CartApi.class);
        promoteApi = retrofitCDN.create(PromoteAPi.class);
        appApi2 =retrofitCDN.create(AppApi2.class);
        develAppApi2 = retrofitCDN.create(DevelAppApi2.class);
        trackApi = retrofitOpenApi.create(TrackApi.class);
        traceApi = retrofitCDN.create(TraceApi.class);
        serverApi = retrofitCDN.create(ServerApi.class);
        dailyNoticeApi = retrofitCDN.create(DailyNoticeApi.class);


    }

    public Api getProductApi() {
        return INSTANCE.api;
    }

    public CartApi getCartApi() {
        return INSTANCE.cartApi;
    }

    public PromoteAPi getPromoteApi() {
        return INSTANCE.promoteApi;
    }

    public AppApi2 getAppApi2() {
        return INSTANCE.appApi2;
    }

    public DevelAppApi2 getDevelAppApi2() {
        return INSTANCE.develAppApi2;
    }

    public TrackApi getTrackApi() {
        return INSTANCE.trackApi;
    }

    public TraceApi getTraceApi() {
        return INSTANCE.traceApi;
    }

    public ServerApi getServerApi() {return INSTANCE.serverApi;}

    private boolean isJsonValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public void changeSide() {
        resetInstance();
        PreferencesTool.getInstance().put(PreferencesKey.IS_DEVEL, !PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class));
    }

    public DailyNoticeApi getDailyNoticeApi() {
        return INSTANCE.dailyNoticeApi;
    }
}
