package com.crazymike.api.service;

import com.crazymike.api.URL;
import com.crazymike.api.response.appapi2.BaseRes;
import com.crazymike.api.response.appapi2.GetAppVersion;
import com.crazymike.api.response.appapi2.GetCueSheetRes;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface DevelAppApi2 {

    @FormUrlEncoded
    @POST(URL.DEVEL_ADD_DEVICE_TOKEN)
    Observable<Object> addDeviceToken(@Field("os") String os, @Field("alias") String alias, @Field("token") String token, @Field("userID") String userID, @Field("deviceId") String deviceId);

    @GET(URL.DEVEL_GET_Q_EVENT)
    Observable<GetCueSheetRes> getCueEvent(@Query("os") String os);

    @FormUrlEncoded
    @POST(URL.DEVEL_ADD_PUSH_TIMES)
    Observable<BaseRes> addPushItem(@Field("token") String token, @Field("times") String times);

    @FormUrlEncoded
    @POST("/clickEvent?callfrom=app")
    Observable<Object> clickEvent(@Field("id") String id, @Field("token") String token);

    @GET(URL.DEVEL_GET_APP_VERSION)
    Observable<GetAppVersion> getAppVersion();
}
