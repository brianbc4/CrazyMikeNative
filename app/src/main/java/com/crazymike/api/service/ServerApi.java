package com.crazymike.api.service;

import com.crazymike.api.URL;
import com.crazymike.api.response.BaseResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ChaoJen on 2017/1/4.
 */

public interface ServerApi {
    @GET(URL.SERVER_LOG)
    Observable<BaseResponse> sendServerLog(
            @Query("session") String sessionId,
            @Query("user") String loginUser,
            @Query("memberId") String memberId,
            @Query("token") String gcmToken,
            @Query("params") String params,
            @Query("target") String target);
}
