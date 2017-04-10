package com.crazymike.respositories;

import android.os.Build;
import android.util.Log;

import com.crazymike.CrazyMike;
import com.crazymike.api.NetworkService;
import com.crazymike.api.response.appapi2.BaseRes;
import com.crazymike.api.response.appapi2.GetAppVersion;
import com.crazymike.api.response.appapi2.GetCueSheetRes;
import com.crazymike.api.service.AppApi2;
import com.crazymike.api.service.DevelAppApi2;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;

import rx.Observable;

public class AppApi2Repository {

    private static AppApi2Repository INSTANCE = new AppApi2Repository();

    private AppApi2 appApi2 =NetworkService.getInstance().getAppApi2();
    private DevelAppApi2 develAppApi2 =NetworkService.getInstance().getDevelAppApi2();


    private AppApi2Repository() {

    }

    public static AppApi2Repository getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new AppApi2Repository());
    }

    public Observable<Object> addDeviceToken(String token){
        if( PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class)) {
            return develAppApi2.addDeviceToken(String.format("Android %s", Build.VERSION.RELEASE),
                    Build.MODEL,
                    token,
                    PreferencesTool.getInstance().get(PreferencesKey.MEMBER_ID, String.class),
                    CrazyMike.IMEI());
        }else{
            return appApi2.addDeviceToken(String.format("Android %s", Build.VERSION.RELEASE),
                    Build.MODEL,
                    token,
                    PreferencesTool.getInstance().get(PreferencesKey.MEMBER_ID, String.class),
                    CrazyMike.IMEI());
        }
    }

    public Observable<GetCueSheetRes> getCueSheet(){
        if(PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class)){
            return develAppApi2.getCueEvent("android");
        }else{
            return appApi2.getCueEvent("android");
        }


    }

    public Observable<Object> clickEvent(String id, String token){
        if(PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class)){
            return develAppApi2.clickEvent(id, token);
        }else{
            return appApi2.clickEvent(id, token);
        }

    }

    public Observable<GetAppVersion> getAppVersion(){
        if(PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class)){
            return develAppApi2.getAppVersion();
        }else{
            return appApi2.getAppVersion();
        }

    }

    public Observable<BaseRes> addPushItem(String token, String times){
        if(PreferencesTool.getInstance().get(PreferencesKey.IS_DEVEL, Boolean.class)){
            return develAppApi2.addPushItem(token, times);
        }else{
            return appApi2.addPushItem(token, times);
        }

    }
}
